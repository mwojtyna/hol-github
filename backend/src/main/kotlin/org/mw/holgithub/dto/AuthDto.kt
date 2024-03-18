package org.mw.holgithub.dto

import org.mw.holgithub.model.UserModel
import java.util.*

data class AuthDto(
    val user: UserModel,
    val sessionId: UUID,
)