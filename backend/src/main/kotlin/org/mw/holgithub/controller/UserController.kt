package org.mw.holgithub.controller

import org.mw.holgithub.service.UserService
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

    @GetMapping("/test")
    fun test(): String {
        return "test"
    }
}
