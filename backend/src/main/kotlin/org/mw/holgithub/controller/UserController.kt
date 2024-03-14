package org.mw.holgithub.controller

import com.sun.net.httpserver.Authenticator.Success
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.mw.holgithub.dto.ApiUserSigninPostRequest
import org.mw.holgithub.dto.ApiUserSignupPostRequest
import org.mw.holgithub.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    private val service: UserService,
) {

    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody body: ApiUserSignupPostRequest): ResponseEntity<Success> {
        service.signUp(body.username, body.password)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/signin")
    fun signIn(
        @Valid @RequestBody body: ApiUserSigninPostRequest,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        service.signIn(body.username, body.password, request, response)
    }

    @PostMapping("/signout")
    fun signOut(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        service.signOut(request, response)
    }

    @GetMapping("/test")
    fun test(): String {
        return "test"
    }
}
