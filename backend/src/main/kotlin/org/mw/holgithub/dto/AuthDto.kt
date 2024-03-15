package org.mw.holgithub.dto

import java.util.*

data class AuthDto(
    val username: String,
    val sessionId: UUID,
)