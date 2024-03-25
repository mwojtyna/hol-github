package org.mw.holgithub.service

import jakarta.persistence.EntityManager
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import org.mw.holgithub.exception.UserNotFoundException
import org.mw.holgithub.model.SessionModel
import org.mw.holgithub.repository.SessionRepository
import org.mw.holgithub.repository.UserRepository
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class SessionService(
    private val repository: SessionRepository,
    private val userRepository: UserRepository,
    private val entityManager: EntityManager,
) {
    companion object {
        const val COOKIE_NAME = "SESSION_ID"
        const val SESSION_MAX_AGE_S = 60 * 60 * 24 * 7
    }

    fun getSessionCookie(request: HttpServletRequest): Cookie? {
        return request.cookies?.find { it.name == COOKIE_NAME }
    }

    fun createSessionCookie(content: String?): Cookie {
        val cookie = Cookie(COOKIE_NAME, content)
        cookie.isHttpOnly = true
        cookie.setAttribute("SameSite", "Strict")
        cookie.secure = false
        cookie.maxAge = SESSION_MAX_AGE_S
        cookie.path = "/api"

        return cookie
    }

    fun deleteSessionCookie(): Cookie {
        val cookie = createSessionCookie(null)
        cookie.maxAge = 0
        return cookie
    }

    fun getSession(sessionId: UUID): SessionModel? {
        // Errors if the session id isn't a UUID or if the session doesn't exist
        try {
            val session = repository.getReferenceById(sessionId)
            if (isExpired(session.expireDate)) {
                deleteSession(sessionId)
                return null
            }
            return session
        } catch (exception: JpaObjectRetrievalFailureException) {
            return null
        }
    }

    /** @throws UserNotFoundException */
    fun createSession(username: String): SessionModel {
        val user =
            userRepository.findByUsername(username) ?: throw UserNotFoundException("User not found")

        val session = SessionModel(
            id = UUID.randomUUID(),
            user = user,
            expireDate = Timestamp(System.currentTimeMillis() + 1000 * SESSION_MAX_AGE_S)
        )
        return repository.save(session)
    }

    fun deleteSession(sessionId: UUID) {
        repository.deleteById(sessionId)
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    fun deleteAllExpired() {
        val query =
            entityManager.createQuery("DELETE FROM SessionModel s WHERE s.expireDate < :now")
        query.setParameter("now", Timestamp(System.currentTimeMillis()))
        query.executeUpdate()
    }

    fun isExpired(date: Timestamp): Boolean {
        return date.before(Timestamp(System.currentTimeMillis()))
    }
}