package org.mw.holgithub.dto

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ApiUserSignupPostRequest(
    @field:Size(
        min = 1,
        max = 255,
        message = "Username must be between 1 and 255 characters"
    ) val username: String,

    @field:Pattern(
        regexp = """^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$""", message = "Invalid password"
    ) val password: String,
)