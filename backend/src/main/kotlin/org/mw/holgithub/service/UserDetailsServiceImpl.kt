package org.mw.holgithub.service

import org.mw.holgithub.exception.UserNotFoundException
import org.mw.holgithub.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val repository: UserRepository) : UserDetailsService {
    /** @throws UserNotFoundException */
    override fun loadUserByUsername(username: String): UserDetails {
        val user = repository.findByUsername(username)
            ?: throw UserNotFoundException(username)

        return User.withUsername(user.username)
            .password(user.password)
            .build()
    }
}