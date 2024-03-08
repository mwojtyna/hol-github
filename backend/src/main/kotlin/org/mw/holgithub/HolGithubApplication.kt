package org.mw.holgithub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class HolGithubApplication

fun main(args: Array<String>) {
    runApplication<HolGithubApplication>(*args)
}
