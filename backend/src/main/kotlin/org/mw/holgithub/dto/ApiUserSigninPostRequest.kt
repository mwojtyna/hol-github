package org.mw.holgithub.dto

import jakarta.validation.constraints.NotEmpty

data class ApiUserSigninPostRequest(
    @field:NotEmpty(message = "Username must not be empty")
    val username: String,

    @field:NotEmpty(message = "Password must not be empty")
    val password: String,
)