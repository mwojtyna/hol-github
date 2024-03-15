package org.mw.holgithub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class])
class HolGithubApplication

fun main(args: Array<String>) {
    runApplication<HolGithubApplication>(*args)
}
