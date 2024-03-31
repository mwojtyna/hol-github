package org.mw.holgithub.service

import jakarta.persistence.EntityManager
import jakarta.persistence.Query
import org.mw.holgithub.dto.ApiGameChoosePostRequestChoice
import org.mw.holgithub.dto.ApiGameChoosePostResponseResult
import org.mw.holgithub.dto.AuthDto
import org.mw.holgithub.model.GameModel
import org.mw.holgithub.model.GameStateModel
import org.mw.holgithub.model.RepoModel
import org.mw.holgithub.repository.GameRepository
import org.mw.holgithub.repository.GameStateRepository
import org.mw.holgithub.repository.SessionRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameService(
    private val repository: GameRepository,
    private val gameStateRepository: GameStateRepository,
    private val sessionRepository: SessionRepository,
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
        val secondRepo = getRandomRepo(firstRepo.id)

        val gameState = GameStateModel(firstRepo = firstRepo, secondRepo = secondRepo)
        gameStateRepository.save(gameState)

        val game = GameModel(user = auth.user, gameState = gameState, score = 0)
        repository.save(game)

        val session = sessionRepository.findById(auth.sessionId).get()
        session.currentGame = game
        sessionRepository.save(session)

        return game
    }

    /** @throws IllegalStateException */
    fun chooseRepo(
        gameId: UUID,
        choice: ApiGameChoosePostRequestChoice,
        auth: AuthDto,
    ): ChooseRepoResult {
        val game = repository.findById(gameId).orElseThrow()
        val gameState = game.gameState
            ?: throw IllegalStateException("Game state is null but session has a current game")
        val session = sessionRepository.findById(auth.sessionId).get()

        val onCorrect = fun(): ChooseRepoResult {
            game.score++
            repository.save(game)

            val nextRepo =
                getRandomRepo(except1 = gameState.firstRepo.id, except2 = gameState.secondRepo.id)
            val secondRepoStarAmount = gameState.secondRepo.starAmount

            gameState.firstRepo = gameState.secondRepo.copy()
            gameState.secondRepo = nextRepo
            gameStateRepository.save(gameState)

            return ChooseRepoResult(
                result = ApiGameChoosePostResponseResult.CORRECT,
                secondRepoStarAmount = secondRepoStarAmount,
                nextRepo = nextRepo
            )
        }
        val onWrong = fun(): ChooseRepoResult {
            game.gameState = null
            repository.save(game)

            session.currentGame = null
            sessionRepository.save(session)

            gameStateRepository.delete(gameState)

            return ChooseRepoResult(
                result = ApiGameChoosePostResponseResult.WRONG,
                secondRepoStarAmount = gameState.secondRepo.starAmount
            )
        }

        when (choice) {
            ApiGameChoosePostRequestChoice.HIGHER -> {
                return if (gameState.secondRepo.starAmount >= gameState.firstRepo.starAmount) {
                    onCorrect()
                } else {
                    onWrong()
                }
            }

            ApiGameChoosePostRequestChoice.LOWER -> {
                return if (gameState.secondRepo.starAmount <= gameState.firstRepo.starAmount) {
                    onCorrect()
                } else {
                    onWrong()
                }
            }
        }
    }

    private fun getRandomRepo(except1: UUID? = null, except2: UUID? = null): RepoModel {
        val countQuery = entityManager.createQuery("SELECT COUNT(r) FROM RepoModel r")
        val count = (countQuery.singleResult as Long).toInt()

        val filteredOutRows: Int
        val selectQuery: Query

        if (except1 == null && except2 == null) {
            filteredOutRows = 0
            selectQuery = entityManager.createQuery("SELECT r FROM RepoModel r")
        } else if ((except1 != null && except2 == null) || (except1 == null && except2 != null)) {
            filteredOutRows = 1
            selectQuery = entityManager.createQuery("SELECT r FROM RepoModel r WHERE r.id <> :id")
                .setParameter("id", except1 ?: except2)
        } else {
            filteredOutRows = 2
            selectQuery =
                entityManager.createQuery("SELECT r FROM RepoModel r WHERE r.id <> :id1 AND r.id <> :id2")
                    .setParameter("id1", except1).setParameter("id2", except2)
        }

        val random = Random().nextInt(count - filteredOutRows)
        val results = selectQuery.resultList as List<*>
        return results[random] as RepoModel
    }
}

data class ChooseRepoResult(
    val result: ApiGameChoosePostResponseResult,
    val secondRepoStarAmount: Int,
    val nextRepo: RepoModel? = null,
)