package org.mw.holgithub.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.mw.holgithub.dto.AuthDto
import org.mw.holgithub.service.SessionService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class SessionFilter(private val service: SessionService) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (request.servletPath == "/user/signin" || request.servletPath == "/user/signup") {
            filterChain.doFilter(request, response)
            return
        }

        val sessionCookie = service.getSessionCookie(request)
        if (sessionCookie == null) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        val sessionIdFromCookie = try {
            UUID.fromString(sessionCookie.value)
        } catch (_: IllegalArgumentException) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        val session = service.getSession(sessionIdFromCookie)
        if (session == null) {
            service.deleteSession(sessionIdFromCookie)
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        SecurityContextHolder.getContext().authentication =
            PreAuthenticatedAuthenticationToken(AuthDto(session.user, session.id), null)

        filterChain.doFilter(request, response)
    }
}