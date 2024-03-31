package com.mw.hol_github_frontend.dto

import androidx.compose.ui.graphics.ImageBitmap

class GameDto(
    val firstImage: ImageBitmap,
    val secondImage: ImageBitmap,
    val repos: ReposDto,
    val score: Int = 0,
)

data class ReposDto(val first: RepoDto, val second: RepoDto)

class RepoDto(val name: String, val description: String, val starAmount: Int?)
