package org.mw.holgithub.dto

data class ApiGameChoosePostRequest(
    val choice: ApiGameChoosePostRequestChoice,
)

enum class ApiGameChoosePostRequestChoice {
    HIGHER,
    LOWER,
}