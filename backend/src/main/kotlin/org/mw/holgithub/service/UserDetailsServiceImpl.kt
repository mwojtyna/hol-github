package org.mw.holgithub.service

import org.mw.holgithub.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val repo: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = repo.findByUsername(username)
            ?: throw Exception("User with username '${username}' not found")

        return User.withUsername(user.username)
            .password(user.password)
            .authorities("USER")
            .build()
    }
}