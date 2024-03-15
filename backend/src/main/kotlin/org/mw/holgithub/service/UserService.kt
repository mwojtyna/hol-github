package org.mw.holgithub.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.mw.holgithub.dto.AuthDto
import org.mw.holgithub.exception.UserAlreadyExistsException
import org.mw.holgithub.model.UserModel
import org.mw.holgithub.repository.UserRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val sessionService: SessionService,
) {
    fun signUp(username: String, password: String) {
        try {
            repository.save(
                UserModel(
                    username = username, password = passwordEncoder.encode(password)
                )
            )
        } catch (_: DataIntegrityViolationException) {
            throw UserAlreadyExistsException(username)
        }
    }

    fun signIn(
        username: String,
        password: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        // Create a new session
        val session = sessionService.createSession(username)

        // Create a new cookie with the session id
        val cookie = sessionService.createSessionCookie(session.id.toString())
        response.addCookie(cookie)
    }

    fun signOut(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @AuthenticationPrincipal auth: AuthDto,
    ) {
        SecurityContextHolder.clearContext()

        // Remove the session from the database
        // Session cookie always exists in this context, because the endpoint is protected
        sessionService.deleteSession(auth.sessionId)

        // Remove the session cookie
        val cookie = sessionService.createSessionCookie(null)
        cookie.maxAge = 0
        response.addCookie(cookie)
    }
}