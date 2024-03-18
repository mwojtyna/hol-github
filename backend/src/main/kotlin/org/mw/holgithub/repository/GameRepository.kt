package org.mw.holgithub.repository

import org.mw.holgithub.model.GameModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface GameRepository : JpaRepository<GameModel, UUID> {
    fun getAllByUserId(userId: UUID): List<GameModel>
}