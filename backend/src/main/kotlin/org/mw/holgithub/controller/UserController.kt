package org.mw.holgithub.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.mw.holgithub.service.UserService
import org.openapitools.client.models.ApiUserSigninPostRequest
import org.openapitools.client.models.ApiUserSignupPostRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    private val service: UserService,
) {

    @PostMapping("/signup")
    fun signUp(@RequestBody body: ApiUserSignupPostRequest) {
        service.signUp(body.username, body.password)
    }

    @PostMapping("/signin")
    fun signIn(
        @RequestBody body: ApiUserSigninPostRequest,
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
