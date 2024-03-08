package org.mw.holgithub.controller

import org.mw.holgithub.model.UserModel
import org.mw.holgithub.repository.UserRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(val repo: UserRepository) {
    @PostMapping("/user/signup")
    fun signUp(): MutableIterable<UserModel> {
        val users = repo.findAll()
        return users
    }
}

