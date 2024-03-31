package com.mw.hol_github_frontend.api.game

data class ApiGameChooseRequest(
    val choice: Choice,
) {
    enum class Choice {
        HIGHER,
        LOWER
    }
}

