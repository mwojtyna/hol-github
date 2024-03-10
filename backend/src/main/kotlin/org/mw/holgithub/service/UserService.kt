package org.mw.holgithub.service

import org.mw.holgithub.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val repo: UserRepository) {
    fun test(): Array<String> {
        return arrayOf("test")
    }
}
