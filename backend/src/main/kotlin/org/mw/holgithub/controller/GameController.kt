package org.mw.holgithub.controller

import org.mw.holgithub.dto.*
import org.mw.holgithub.service.GameService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayInputStream
import java.net.URLConnection

@RestController
@RequestMapping("/game")
class GameController(private val service: GameService) {
    @PostMapping("/new", produces = ["multipart/form-data"])
    fun new(@AuthenticationPrincipal auth: AuthDto): ResponseEntity<LinkedMultiValueMap<String, Any>> {
        val game = service.createGame(auth)
        val firstRepo = RepoDto(
            game.gameState.firstRepo.name, game.gameState.firstRepo.description
        )
        val secondRepo = RepoDto(
            game.gameState.secondRepo.name, game.gameState.secondRepo.description
        )

        val gameIdHeaders = LinkedMultiValueMap<String, String>()
        gameIdHeaders.set("Content-Type", "text/plain")

        val firstImageHeaders = LinkedMultiValueMap<String, String>()
        firstImageHeaders.set(
            "Content-Type",
            URLConnection.guessContentTypeFromStream(ByteArrayInputStream(game.gameState.firstRepo.image))
        )

        val secondImageHeaders = LinkedMultiValueMap<String, String>()
        secondImageHeaders.set(
            "Content-Type",
            URLConnection.guessContentTypeFromStream(ByteArrayInputStream(game.gameState.secondRepo.image))
        )

        val reposHeaders = LinkedMultiValueMap<String, String>()
        reposHeaders.set("Content-Type", "application/json")

        val formData = LinkedMultiValueMap<String, Any>()
        formData.add("gameId", HttpEntity(game.id.toString(), gameIdHeaders))
        formData.add("firstImage", HttpEntity(game.gameState.firstRepo.image, firstImageHeaders))
        formData.add("secondImage", HttpEntity(game.gameState.secondRepo.image, secondImageHeaders))
        formData.add(
            "repos",
            HttpEntity(ApiGameNewPostResponseReposPart(firstRepo, secondRepo), reposHeaders)
        )

        return ResponseEntity(formData, HttpStatus.CREATED)
    }

    @PostMapping("/choose")
    fun choose(@RequestBody body: ApiGameChoosePostRequest): ResponseEntity<Any> {
        val result = service.chooseRepo(body.gameId, body.choice)
        when (result.result) {
            ApiGameChoosePostResponseResult.CORRECT -> {
                val nextRepo = result.nextRepo!!

                val formData = LinkedMultiValueMap<String, Any>()
                val headers = LinkedMultiValueMap<String, String>()
                headers.set("Content-Type", "multipart/form-data")

                val resultHeaders = LinkedMultiValueMap<String, String>()
                resultHeaders.set("Content-Type", "text/plain")

                val nextImageHeaders = LinkedMultiValueMap<String, String>()
                nextImageHeaders.set(
                    "Content-Type",
                    URLConnection.guessContentTypeFromStream(ByteArrayInputStream(nextRepo.image))
                )

                val nextRepoHeaders = LinkedMultiValueMap<String, String>()
                nextRepoHeaders.set("Content-Type", "application/json")

                formData.set(
                    "result",
                    HttpEntity(
                        ApiGameChoosePostResponseResult.CORRECT.toString(),
                        resultHeaders
                    )
                )
                formData.set("nextImage", HttpEntity(nextRepo.image, nextImageHeaders))
                formData.set(
                    "nextRepo",
                    HttpEntity(RepoDto(nextRepo.name, nextRepo.description), nextRepoHeaders)
                )

                return ResponseEntity(
                    formData,
                    headers,
                    HttpStatus.OK
                )
            }

            ApiGameChoosePostResponseResult.WRONG -> {
                val headers = LinkedMultiValueMap<String, String>()
                headers.set("Content-Type", "application/json")

                return ResponseEntity(
                    ApiGameChoosePostResponse(ApiGameChoosePostResponseResult.WRONG),
                    headers,
                    HttpStatus.OK
                )
            }
        }
    }
}