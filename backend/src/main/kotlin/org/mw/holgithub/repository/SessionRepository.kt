package org.mw.holgithub.repository

import jakarta.transaction.Transactional
import org.mw.holgithub.model.SessionModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SessionRepository : JpaRepository<SessionModel, UUID> {
    @Transactional
    fun deleteAllByUserId(userId: UUID): List<SessionModel>
}