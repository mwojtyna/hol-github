package org.mw.holgithub.controller

import org.mw.holgithub.dto.*
import org.mw.holgithub.repository.SessionRepository
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
class GameController(private val service: GameService, private val sessionRepo: SessionRepository) {
    @PostMapping("/new", produces = ["multipart/related"])
    fun new(@AuthenticationPrincipal auth: AuthDto): ResponseEntity<LinkedMultiValueMap<String, Any>> {
        val game = service.createGame(auth)
        val gameState = game.gameState!!

        val firstRepo = RepoDto(
            name = gameState.firstRepo.name,
            description = gameState.firstRepo.description,
            starAmount = gameState.firstRepo.starAmount
        )
        val secondRepo = RepoDto(
            name = gameState.secondRepo.name,
            description = gameState.secondRepo.description,
            starAmount = null
        )

        val firstImageHeaders = LinkedMultiValueMap<String, String>()
        firstImageHeaders.set(
            "Content-Type",
            URLConnection.guessContentTypeFromStream(ByteArrayInputStream(gameState.firstRepo.image))
        )

        val secondImageHeaders = LinkedMultiValueMap<String, String>()
        secondImageHeaders.set(
            "Content-Type",
            URLConnection.guessContentTypeFromStream(ByteArrayInputStream(gameState.secondRepo.image))
        )

        val reposHeaders = LinkedMultiValueMap<String, String>()
        reposHeaders.set("Content-Type", "application/json")

        val formData = LinkedMultiValueMap<String, Any>()
        formData.add("firstImage", HttpEntity(gameState.firstRepo.image, firstImageHeaders))
        formData.add("secondImage", HttpEntity(gameState.secondRepo.image, secondImageHeaders))
        formData.add(
            "repos",
            HttpEntity(ApiGameNewPostResponseReposPart(firstRepo, secondRepo), reposHeaders)
        )

        return ResponseEntity(formData, HttpStatus.CREATED)
    }

    @PostMapping("/choose")
    fun choose(
        @RequestBody body: ApiGameChoosePostRequest,
        @AuthenticationPrincipal auth: AuthDto,
    ): ResponseEntity<Any> {
        val session = sessionRepo.findById(auth.sessionId).get()
        if (session.currentGame == null) {
            throw NoSuchElementException()
        }

        val chooseRepoResult = service.chooseRepo(session.currentGame!!.id!!, body.choice, auth)

        val formData = LinkedMultiValueMap<String, Any>()
        val headers = LinkedMultiValueMap<String, String>()
        headers.set("Content-Type", "multipart/related")

        val textPlainHeaders = LinkedMultiValueMap<String, String>()
        textPlainHeaders.set("Content-Type", "text/plain")

        formData.set(
            "result", HttpEntity(
                chooseRepoResult.result.toString(), textPlainHeaders
            )
        )
        formData.set(
            "secondRepoStarAmount", HttpEntity(
                chooseRepoResult.secondRepoStarAmount.toString(), textPlainHeaders
            )
        )

        when (chooseRepoResult.result) {
            ApiGameChoosePostResponseResult.CORRECT -> {
                val nextRepo = chooseRepoResult.nextRepo!!

                val nextImageHeaders = LinkedMultiValueMap<String, String>()
                nextImageHeaders.set(
                    "Content-Type",
                    URLConnection.guessContentTypeFromStream(ByteArrayInputStream(nextRepo.image))
                )

                val nextRepoHeaders = LinkedMultiValueMap<String, String>()
                nextRepoHeaders.set("Content-Type", "application/json")

                formData.set("nextImage", HttpEntity(nextRepo.image, nextImageHeaders))
                formData.set(
                    "nextRepo", HttpEntity(
                        RepoDto(
                            name = nextRepo.name,
                            description = nextRepo.description,
                            starAmount = null
                        ), nextRepoHeaders
                    )
                )

                return ResponseEntity(
                    formData, headers, HttpStatus.OK
                )
            }

            ApiGameChoosePostResponseResult.WRONG -> {
                return ResponseEntity(
                    formData, headers, HttpStatus.OK
                )
            }
        }
    }
}