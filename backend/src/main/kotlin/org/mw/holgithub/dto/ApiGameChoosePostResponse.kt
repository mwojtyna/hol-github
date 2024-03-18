package org.mw.holgithub.dto

class ApiGameChoosePostResponse(
    val result: ApiGameChoosePostResponseResult,
)


enum class ApiGameChoosePostResponseResult {
    CORRECT, WRONG;
}