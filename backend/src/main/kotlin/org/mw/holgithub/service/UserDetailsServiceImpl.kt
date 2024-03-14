package org.mw.holgithub.service

import org.mw.holgithub.exception.UserExistsException
import org.mw.holgithub.repository.UserRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val repository: UserRepository) : UserDetailsService {
    /** @throws UserExistsException */
    override fun loadUserByUsername(username: String): UserDetails {
        val user = repository.findByUsername(username)
            ?: throw BadCredentialsException(null)

        return User.withUsername(user.username)
            .password(user.password)
            .build()
    }
}