package org.mw.holgithub.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.mw.holgithub.service.UserService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SessionFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (request.servletPath == "/user/signin" || request.servletPath == "/user/signup") {
            filterChain.doFilter(request, response)
            return
        }

        val sessionCookie = request.cookies?.find { it.name == UserService.COOKIE_NAME }
        if (sessionCookie == null) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        val sessionId = sessionCookie.value
        // TODO: Get session from db
        val session = request.getSession(false)
        if (session == null || sessionId != session.id) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        filterChain.doFilter(request, response)
    }
}
