package main

import (
	"encoding/json"
	"io"
	"log"
	"net/http"
	"os"
	"time"
)

type ResponseBody struct {
	TotalCount        float64          `json:"total_count"`
	IncompleteResults bool             `json:"incomplete_results"`
	Items             []map[string]any `json:"items"`
}
type Repo struct {
	url   string
	stars uint
}

const RETRY_DELAY_S = 10

var logger = log.New(os.Stderr, "", log.Ldate|log.Ltime)

func main() {
	res := fetchRepos()
	for i := 0; res.IncompleteResults == true; i++ {
		logger.Printf("Incomplete results, retrying in %ds...", RETRY_DELAY_S)
		time.Sleep(RETRY_DELAY_S * time.Second)
		fetchRepos()
	}

	var repos []Repo
	for _, v := range res.Items {
		repos = append(repos, Repo{
			url: readJsonProperty[string](v, "html_url"),
			stars: uint(readJsonProperty[float64](v, "stargazers_count")),
		})
	}
}

func fetchRepos() ResponseBody {
	logger.Println("Fetching top 100 repositories by stars...")

	res, err := http.Get("https://api.github.com/search/repositories?q=stars:>0&sort=stars&order=desc&per_page=100")
	if err != nil {
		logger.Fatal(err.Error())
	}
	if res.StatusCode >= 400 {
		logger.Fatalf("Error status code of %d", res.StatusCode)
	}

	defer res.Body.Close()
	body, err := io.ReadAll(res.Body)
	if err != nil {
		logger.Fatal(err.Error())
	}

	var parsed ResponseBody
	err = json.Unmarshal(body, &parsed)
	if err != nil {
		logger.Fatal(err.Error())
	}

	return parsed
}

func readJsonProperty[T any](object map[string]any, name string) T {
	prop, ok := object[name].(T)
	if !ok {
		logger.Fatalf("Could not read '%s' property", name)
	}

	return prop
}
