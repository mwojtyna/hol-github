package org.mw.holgithub.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.mw.holgithub.dto.AuthDto
import org.mw.holgithub.exception.UserAlreadyExistsException
import org.mw.holgithub.model.UserModel
import org.mw.holgithub.repository.UserRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val sessionService: SessionService,
) {
    companion object {
        const val EMPTY_PASSWORD_HASHED =
            "\$argon2i\$v=19\$m=19456,t=2,p=1\$vq+PjhCuFdb2QP7duk9Ftg\$vPm1zQHyVacvlN1GVRlPnqd1N3sgABPEYo3eCR2D16k";
    }

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
        val user = repository.findByUsername(username)
        if (!passwordEncoder.matches(password, user?.password ?: EMPTY_PASSWORD_HASHED)) {
            throw BadCredentialsException("Username or password is incorrect")
        }

        val previousCookie = sessionService.getSessionCookie(request)
        if (previousCookie != null) {
            try {
                val sessionId = UUID.fromString(previousCookie.value)
                sessionService.deleteSession(sessionId)
            } catch (_: IllegalArgumentException) {
            }
        }

        // Create a new session
        val session = sessionService.createSession(username)

        // Create a new cookie with the session id
        val newCookie = sessionService.createSessionCookie(session.id.toString())
        response.addCookie(newCookie)
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