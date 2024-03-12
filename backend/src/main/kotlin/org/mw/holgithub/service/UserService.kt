package org.mw.holgithub.service

import org.mw.holgithub.exceptions.UserExistsException
import org.mw.holgithub.model.UserModel
import org.mw.holgithub.repository.UserRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val repo: UserRepository, private val passwordEncoder: PasswordEncoder) {
    /** @throws UserExistsException */
    fun createUser(username: String, password: String) {
        val encodedPassword = passwordEncoder.encode(password)!!
        val user = UserModel(username = username, password = encodedPassword)

        try {
            repo.save(user)
        } catch (e: DataIntegrityViolationException) {
            throw UserExistsException(user.username)
        }
    }
}
