package org.mw.holgithub.controller

import org.mw.holgithub.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(private val service: UserService) {
    @PostMapping("/signup")
    fun signUp(): String {
        return "signup"
    }

    @PostMapping("/signin")
    fun signIn(): String {
        return "signin"
    }

    @PostMapping("/signout")
    fun signOut(): Unit {
    }
}

