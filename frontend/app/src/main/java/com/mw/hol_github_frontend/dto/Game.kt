package com.mw.hol_github_frontend.dto

import androidx.compose.ui.graphics.ImageBitmap

data class GameDto(
    val firstImage: ImageBitmap,
    val secondImage: ImageBitmap,
    val repos: ReposDto,
    val score: Int = 0,
)

data class ReposDto(val first: RepoDto, val second: RepoDto)

data class RepoDto(
    val name: String,
    val description: String,
    val starAmount: Int?,
    val result: Result? = null,
) {
    enum class Result {
        CORRECT,
        WRONG
    }
}
