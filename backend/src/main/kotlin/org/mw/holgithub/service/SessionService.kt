package org.mw.holgithub.service

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.mw.holgithub.exception.UserNotFoundException
import org.mw.holgithub.model.SessionModel
import org.mw.holgithub.repository.SessionRepository
import org.mw.holgithub.repository.UserRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class SessionService(
    private val repository: SessionRepository,
    private val userRepository: UserRepository,
) {
    companion object {
        const val COOKIE_NAME = "SESSION_ID"
    }

    fun getSessionCookie(request: HttpServletRequest): Cookie? {
        return request.cookies?.find { it.name == COOKIE_NAME }
    }

    fun createSessionCookie(content: String?): Cookie {
        val cookie = Cookie(COOKIE_NAME, content)
        cookie.isHttpOnly = true
        cookie.path = "/api"
        cookie.secure = true

        return cookie
    }

    fun getSession(sessionId: UUID): SessionModel? {
        try {
            val session = repository.getReferenceById(sessionId)
            if (session.expireDate.before(Timestamp(System.currentTimeMillis()))) {
                deleteSession(sessionId)
                return null
            }

            return session
        } catch (exception: Exception) {
            // Errors if the session id isn't a UUID or if the session doesn't exist
            return null
        }
    }

    /** @throws UserNotFoundException */
    fun createSession(username: String): SessionModel {
        val user = userRepository.findByUsername(username)
            ?: throw UserNotFoundException("User not found")

        repository.deleteAllByUserId(user.id!!)

        val session = SessionModel(
            id = UUID.randomUUID(),
            user = user,
            expireDate = Timestamp(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)
        )
        return repository.save(session)
    }

    fun deleteSession(sessionId: UUID) {
        val session = repository.findById(sessionId).getOrNull() ?: return;
        repository.deleteAllByUserId(session.user.id!!)
    }
}