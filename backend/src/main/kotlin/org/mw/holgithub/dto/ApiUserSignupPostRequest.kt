package org.mw.holgithub.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

data class ApiUserSignupPostRequest(
    @field:NotEmpty(message = "Username must not be empty") val username: String,

    @field:Pattern(
        regexp = """^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$""", message = "Invalid password"
    ) val password: String,
)