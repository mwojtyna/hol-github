package org.mw.holgithub.service

import jakarta.persistence.EntityManager
import org.mw.holgithub.dto.ApiGameChoosePostRequestChoice
import org.mw.holgithub.dto.ApiGameChoosePostResponseResult
import org.mw.holgithub.dto.AuthDto
import org.mw.holgithub.exception.GameAlreadyEndedException
import org.mw.holgithub.model.GameModel
import org.mw.holgithub.model.GameStateModel
import org.mw.holgithub.model.RepoModel
import org.mw.holgithub.repository.GameRepository
import org.mw.holgithub.repository.GameStateRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameService(
    private val repository: GameRepository,
    private val gameStateRepository: GameStateRepository,
    private val entityManager: EntityManager,
) {
    fun createGame(auth: AuthDto): GameModel {
        for (game in repository.getAllByUserId(auth.user.id!!)) {
            if (game.gameState == null) {
                continue
            }

            val gameState = game.gameState!!
            game.gameState = null
            repository.save(game)
            gameStateRepository.delete(gameState)
        }

        val firstRepo = getRandomRepo()
        var secondRepo = getRandomRepo()
        while (secondRepo.id == firstRepo.id) {
            secondRepo = getRandomRepo()
        }

        val gameState = GameStateModel(firstRepo = firstRepo, secondRepo = secondRepo)
        gameStateRepository.save(gameState)

        val game = GameModel(user = auth.user, gameState = gameState, score = 0)
        repository.save(game)

        return game
    }

    /** @throws GameAlreadyEndedException */
    fun chooseRepo(
        gameId: UUID,
        choice: ApiGameChoosePostRequestChoice,
    ): ChooseRepoResult {
        val game = repository.findById(gameId).orElseThrow()
        val gameState = game.gameState ?: throw GameAlreadyEndedException()

        val onCorrect = fun(): RepoModel {
            game.score++
            repository.save(game)

            val nextRepo = getRandomRepo()
            gameState.firstRepo = gameState.secondRepo
            gameState.secondRepo = nextRepo
            gameStateRepository.save(gameState)

            return nextRepo
        }
        val onWrong = fun() {
            game.gameState = null
            repository.save(game)
            gameStateRepository.delete(gameState)
        }

        when (choice) {
            ApiGameChoosePostRequestChoice.HIGHER -> {
                if (gameState.secondRepo.starAmount >= gameState.firstRepo.starAmount) {
                    val nextRepo = onCorrect()
                    return ChooseRepoResult(ApiGameChoosePostResponseResult.CORRECT, nextRepo)
                } else {
                    onWrong()
                    return ChooseRepoResult(ApiGameChoosePostResponseResult.WRONG)
                }
            }

            ApiGameChoosePostRequestChoice.LOWER -> {
                if (gameState.secondRepo.starAmount <= gameState.firstRepo.starAmount) {
                    val nextRepo = onCorrect()
                    return ChooseRepoResult(ApiGameChoosePostResponseResult.CORRECT, nextRepo)
                } else {
                    onWrong()
                    return ChooseRepoResult(ApiGameChoosePostResponseResult.WRONG)
                }
            }
        }
    }

    private fun getRandomRepo(): RepoModel {
        val countQuery = entityManager.createQuery("SELECT COUNT(r) FROM RepoModel r")
        val count = countQuery.singleResult as Long

        val random = Random()
        val number = random.nextInt(count.toInt())

        val selectQuery = entityManager.createQuery("SELECT r FROM RepoModel r")
        selectQuery.firstResult = number
        selectQuery.maxResults = 1

        return selectQuery.singleResult as RepoModel
    }
}

data class ChooseRepoResult(
    val result: ApiGameChoosePostResponseResult,
    val nextRepo: RepoModel? = null,
)