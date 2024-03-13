package org.mw.holgithub.service

import org.mw.holgithub.exception.UserExistsException
import org.mw.holgithub.model.UserModel
import org.mw.holgithub.repository.UserRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    /** @throws UserExistsException */
    fun signUp(username: String, password: String) {
        try {
            repository.save(
                UserModel(
                    username = username,
                    password = passwordEncoder.encode(password)
                )
            )
        } catch (e: DataIntegrityViolationException) {
            throw UserExistsException("User with username '$username' already exists")
        }
    }
}