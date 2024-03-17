package main

import (
	"context"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"net/url"
	"os"
	"strings"
	"sync"
	"time"

	"github.com/charmbracelet/log"
	"github.com/google/uuid"
	"github.com/h2non/bimg"
	"github.com/jackc/pgx/v5"
	"github.com/joho/godotenv"
)

type (
	ResponseBody struct {
		TotalCount        float64          `json:"total_count"`
		IncompleteResults bool             `json:"incomplete_results"`
		Items             []map[string]any `json:"items"`
	}
	Repo struct {
		url         *url.URL
		name        string
		description string
		stars       uint
		image       []byte
	}

	FetchRepoChan       chan FetchRepoReturnType
	FetchRepoReturnType struct {
		repo  Repo
		error error
	}
)

const (
	RETRY_DELAY_S          = 10
	OG_IMAGE_PROPERTY_NAME = `"og:image"`
	OG_IMAGE_HOST          = "opengraph.githubassets.com"
	OG_IMAGE_HEIGHT        = 600 - 150
)

var logger = log.NewWithOptions(os.Stderr, log.Options{
	ReportCaller:    true,
	ReportTimestamp: true,
	Level:           log.DebugLevel,
})

func main() {
	env, err := godotenv.Read()
	if err != nil {
		logger.Fatal("Error loading .env file", "error", err.Error())
	}
	dbUrl := GetEnv("DB_URL", env)
	noCheck := GetEnv("NO_INCOMPLETE_CHECK", env)

	ctx := context.Background()
	conn, err := pgx.Connect(ctx, dbUrl)
	if err != nil {
		logger.Fatal("Connection with db failed", "db_url", dbUrl)
	}
	defer conn.Close(ctx)

	_, err1 := conn.Exec(ctx, `TRUNCATE TABLE "repo"`)
	if err1 != nil {
		logger.Fatal("Failed to truncate table", "error", err1.Error())
	}

	res, err := fetchGhApi()
	if err != nil {
		logger.Fatal("Failed fetching GitHub API", "error", err.Error())
	}

	for i := 0; noCheck == "" && res.IncompleteResults == true; i++ {
		logger.Warn(fmt.Sprintf("Incomplete results, retrying in %ds...", RETRY_DELAY_S))
		time.Sleep(RETRY_DELAY_S * time.Second)
		fetchGhApi()
	}

	ch := make(FetchRepoChan)
	var wg sync.WaitGroup
	for _, item := range res.Items {
		wg.Add(1)
		go fetchRepo(item, ch, &wg)
	}

	go func() {
		wg.Wait()
		close(ch)
	}()

	var repos []Repo
	for result := range ch {
		if result.error != nil {
			continue
		}

		repo := result.repo
		repos = append(repos, repo)

		id, err := uuid.NewRandom()
		if err != nil {
			logger.Fatal("Failed generating UUID", "error", err.Error())
		}

		logger.Debug("Inserting repo into db", "repo", repo.name)
		_, err1 := conn.Exec(context.Background(), `INSERT INTO "repo" (id, name, description, star_amount, image) VALUES ($1, $2, $3, $4, $5)`, id, repo.name, repo.description, repo.stars, repo.image)
		if err1 != nil {
			logger.Fatal("Failed inserting", "repo_name", repo, "error", err1.Error())
		}
	}

	logger.Info("Saved OG images successfully")
}

func fetchGhApi() (ResponseBody, error) {
	logger.Info("Fetching top 100 repositories by stars")

	res, err := http.Get("https://api.github.com/search/repositories?q=stars:>0&sort=stars&order=desc&per_page=100")
	if err != nil {
		return ResponseBody{}, err
	}
	if res.StatusCode >= 400 {
		return ResponseBody{}, StatusCodeError{Code: res.StatusCode}
	}

	defer res.Body.Close()
	body, err := io.ReadAll(res.Body)
	if err != nil {
		return ResponseBody{}, err
	}

	var parsed ResponseBody
	err = json.Unmarshal(body, &parsed)
	if err != nil {
		return ResponseBody{}, err
	}

	return parsed, nil
}

func fetchRepo(item map[string]any, ch FetchRepoChan, wg *sync.WaitGroup) {
	defer wg.Done()

	url, err := url.Parse(GetJsonProperty[string](item, "html_url"))
	if err != nil {
		logger.Warn("Skipping repo because of error", "repo_url", url, "error", err.Error())
		ch <- FetchRepoReturnType{Repo{}, err}
	}

	img, err := fetchImage(url)
	if err != nil {
		logger.Warn("Skipping repo because of error", "repo_url", url, "error", err.Error())
		ch <- FetchRepoReturnType{Repo{}, err}
	}

	repo := Repo{
		url:         url,
		name:        GetJsonProperty[string](item, "full_name"),
		description: GetJsonProperty[string](item, "description"),
		stars:       uint(GetJsonProperty[float64](item, "stargazers_count")),
		image:       img,
	}
	ch <- FetchRepoReturnType{repo, nil}
}

func fetchImage(repoUrl *url.URL) ([]byte, error) {
	imgUrl, err := getImageUrl(repoUrl)
	if err != nil {
		return nil, err
	}

	logger.Debug("Downloading OG image", "repo_url", repoUrl.String())

	res, err := http.Get(imgUrl.String())
	if err != nil {
		return nil, err
	}
	if res.StatusCode >= 400 {
		return nil, StatusCodeError{Code: res.StatusCode}
	}

	defer res.Body.Close()
	body, err := io.ReadAll(res.Body)
	if err != nil {
		return nil, err
	}

	img := bimg.NewImage(body)

	// If custom og image skip hiding stars
	if imgUrl.Host != OG_IMAGE_HOST {
		return body, nil
	}

	size, err := img.Size()
	if err != nil {
		return nil, err
	}

	logger.Debug("Cropping OG image", "repo_url", repoUrl.String())
	resized, err := img.Crop(size.Width, OG_IMAGE_HEIGHT, bimg.GravityNorth)
	if err != nil {
		return nil, err
	}

	return resized, nil
}
func getImageUrl(repoUrl *url.URL) (*url.URL, error) {
	logger.Debug("Getting OG image url", "repo_url", repoUrl.String())

	res, err := http.Get(repoUrl.String())
	if err != nil {
		return nil, err
	}
	if res.StatusCode >= 400 {
		return nil, StatusCodeError{Code: res.StatusCode}
	}

	defer res.Body.Close()
	body, err := io.ReadAll(res.Body)
	if err != nil {
		return nil, err
	}

	repoHtml := string(body)
	propNameIndex := strings.Index(repoHtml, OG_IMAGE_PROPERTY_NAME)
	startQuoteIndex := propNameIndex + len(OG_IMAGE_PROPERTY_NAME)
	for repoHtml[startQuoteIndex] != '"' {
		startQuoteIndex++
	}
	endQuoteIndex := startQuoteIndex + 1
	for repoHtml[endQuoteIndex] != '"' {
		endQuoteIndex++
	}

	imgUrl, err := url.Parse(repoHtml[startQuoteIndex+1 : endQuoteIndex])
	if err != nil {
		return nil, err
	}

	return imgUrl, nil
}
