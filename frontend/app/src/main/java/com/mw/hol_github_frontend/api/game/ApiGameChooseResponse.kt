package com.mw.hol_github_frontend.api.game


data class ApiGameChooseResponse(
    val result: Result,
) {
    enum class Result {
        CORRECT,
        WRONG
    }
}