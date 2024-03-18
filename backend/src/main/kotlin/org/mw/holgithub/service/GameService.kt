package org.mw.holgithub.service

import jakarta.persistence.EntityManager
import org.mw.holgithub.dto.AuthDto
import org.mw.holgithub.model.GameModel
import org.mw.holgithub.model.GameStateModel
import org.mw.holgithub.model.RepoModel
import org.mw.holgithub.repository.GameRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameService(
    private val repository: GameRepository,
    private val entityManager: EntityManager,
) {
    fun createGame(auth: AuthDto): GameModel {
        val firstRepo = getRandomRepo()
        var secondRepo = getRandomRepo()
        while (secondRepo.id == firstRepo.id) {
            secondRepo = getRandomRepo()
        }

        val gameState = GameStateModel(firstRepo = firstRepo, secondRepo = secondRepo)
        val game = GameModel(user = auth.user, gameState = gameState, score = 0)
        repository.save(game)

        return game
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