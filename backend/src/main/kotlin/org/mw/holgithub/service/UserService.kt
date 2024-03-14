package org.mw.holgithub.service

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.mw.holgithub.exception.UserExistsException
import org.mw.holgithub.model.UserModel
import org.mw.holgithub.repository.UserRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
) {
    companion object {
        const val COOKIE_NAME = "SESSION_ID"
    }

    private val securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy()
    private val logoutHandler = SecurityContextLogoutHandler()

    /** @throws UserExistsException */
    fun signUp(username: String, password: String) {
        try {
            repository.save(
                UserModel(
                    username = username, password = passwordEncoder.encode(password)
                )
            )
        } catch (e: DataIntegrityViolationException) {
            throw UserExistsException("User with username '$username' already exists")
        }
    }

    fun signIn(
        username: String,
        password: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        val token = UsernamePasswordAuthenticationToken.unauthenticated(username, password)
        val authentication = authenticationManager.authenticate(token)

        val context = securityContextHolderStrategy.createEmptyContext()
        context.authentication = authentication
        securityContextHolderStrategy.context = context

        // TODO: Save session to db
        // Create a new session
        val session = request.getSession(true)
        session.setAttribute("username", username)

        // Create a new cookie with the session id
        val cookie = createSessionCookie(session.id)
        response.addCookie(cookie)
    }

    fun signOut(request: HttpServletRequest, response: HttpServletResponse) {
        logoutHandler.logout(
            request, response, securityContextHolderStrategy.context.authentication
        )

        // Remove the session cookie
        val cookie = createSessionCookie(null)
        cookie.maxAge = 0
        response.addCookie(cookie)
    }

    private fun createSessionCookie(content: String?): Cookie {
        val cookie = Cookie(COOKIE_NAME, content)
        cookie.isHttpOnly = true
        cookie.path = "/api"

        return cookie
    }
}