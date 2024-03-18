package org.mw.holgithub.dto

import java.util.*

data class ApiGameChoosePostRequest(
    val gameId: UUID,
    val choice: ApiGameChoosePostRequestChoice,
)

enum class ApiGameChoosePostRequestChoice {
    HIGHER,
    LOWER,
}