package com.mw.hol_github_frontend.screen.main.game

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.mw.hol_github_frontend.api.ApiClient
import com.mw.hol_github_frontend.dto.GameDto
import com.mw.hol_github_frontend.dto.ReposDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MultipartReader
import java.nio.charset.Charset
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val apiClient: ApiClient,
    private val gson: Gson,
) : ViewModel() {
    val game = MutableStateFlow<GameDto?>(null)

    suspend fun newGame() {
        val body = apiClient.game.new()
        val reader = MultipartReader(body)

        val firstImageBytes = reader.nextPart()?.body?.readByteArray()
        val secondImageBytes = reader.nextPart()?.body?.readByteArray()
        val reposJson = reader.nextPart()?.body?.readString(Charset.defaultCharset())
        val repos = gson.fromJson(reposJson, ReposDto::class.java)

        if (firstImageBytes == null || secondImageBytes == null || repos == null) {
            throw IllegalStateException("Invalid response")
        }

        val firstImage =
            BitmapFactory.decodeByteArray(firstImageBytes, 0, firstImageBytes.size).asImageBitmap()
        val secondImage = BitmapFactory.decodeByteArray(secondImageBytes, 0, secondImageBytes.size)
            .asImageBitmap()

        game.value = GameDto(firstImage, secondImage, repos)
        reader.close()
    }
}