package org.mw.holgithub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class])
class HolGithubApplication

fun main(args: Array<String>) {
    runApplication<HolGithubApplication>(*args)
}
