package org.mw.holgithub.controller

import org.mw.holgithub.service.UserService
import org.openapitools.client.models.ApiUserSignupPostRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(private val service: UserService) {
    @PostMapping("/signup")
    fun signUp(@RequestBody body: ApiUserSignupPostRequest) {
        service.createUser(body.username, body.password)
    }

    @PostMapping("/signin")
    fun signIn(): String {
        return "signin"
    }

    @PostMapping("/signout")
    fun signOut() {

    }
}

