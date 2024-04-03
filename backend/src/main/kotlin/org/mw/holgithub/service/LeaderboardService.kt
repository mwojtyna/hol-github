package org.mw.holgithub.service

import jakarta.persistence.EntityManager
import org.springframework.stereotype.Service

@Service
class LeaderboardService(private val entityManager: EntityManager) {
    fun getUserScores(): List<LeaderboardEntry> {
        val query = entityManager.createNativeQuery(
            """
                WITH max_per_user AS (
                    SELECT user_id, MAX(score) AS score FROM "game"
                    GROUP BY user_id
                )
                
                SELECT u.username, g.score FROM "user" AS u
                INNER JOIN max_per_user AS g ON g.user_id = u.id
                ORDER BY g.score DESC
                LIMIT 100
                """.trimIndent()
        )

        val leaderboard = query.resultList as List<Array<Any>>
        return leaderboard.map {
            LeaderboardEntry(username = it[0] as String, score = it[1] as Int)
        }
    }
}

data class LeaderboardEntry(
    val username: String,
    val score: Int,
)