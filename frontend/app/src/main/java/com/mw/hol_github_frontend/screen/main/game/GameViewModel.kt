package com.mw.hol_github_frontend.screen.main.game

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.mw.hol_github_frontend.api.ApiClient
import com.mw.hol_github_frontend.api.game.ApiGameChooseRequest
import com.mw.hol_github_frontend.dto.GameDto
import com.mw.hol_github_frontend.dto.RepoDto
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
    companion object {
        const val CORRECT_DELAY = 1500L
        const val WRONG_DELAY = 2000L
    }

    val game = MutableStateFlow<GameDto?>(null)

    suspend fun newGame() {
        val body = apiClient.game.new()
        val reader = MultipartReader(body)

        reader.use {
            val firstImage = reader.nextPart()?.body?.readByteArray()?.let {
                BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
            }
            val secondImage = reader.nextPart()?.body?.readByteArray()?.let {
                BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap()
            }
            val repos = reader.nextPart()?.body?.readString(Charset.defaultCharset())
                ?.let { gson.fromJson(it, ReposDto::class.java) }

            if (firstImage == null || secondImage == null || repos == null) {
                throw IllegalStateException("Invalid response")
            }

            game.value = GameDto(firstImage, secondImage, repos)
        }
    }

    suspend fun choose(choice: ApiGameChooseRequest.Choice): RepoDto.Result {
        val body = apiClient.game.choose(ApiGameChooseRequest(choice))
        val reader = MultipartReader(body)

        reader.use {
            val result = reader.nextPart()?.body?.readString(Charset.defaultCharset())
                ?.let { RepoDto.Result.valueOf(it) }
                ?: throw IllegalStateException("Invalid response")
            val secondRepoStars =
                reader.nextPart()?.body?.readString(Charset.defaultCharset())?.toInt()
                    ?: throw IllegalStateException("Invalid response")

            when (result) {
                RepoDto.Result.CORRECT -> {
                    val nextImage = reader.nextPart()?.body?.readByteArray()
                        ?.let { BitmapFactory.decodeByteArray(it, 0, it.size).asImageBitmap() }
                    val nextRepo = reader.nextPart()?.body?.readString(Charset.defaultCharset())
                        ?.let { gson.fromJson(it, RepoDto::class.java) }

                    if (nextImage == null || nextRepo == null) {
                        throw IllegalStateException("Invalid response")
                    }

                    val newFirstRepo = RepoDto(
                        name = game.value!!.repos.second.name,
                        description = game.value!!.repos.second.description,
                        starAmount = secondRepoStars
                    )

                    game.value = game.value!!.copy(
                        repos = game.value!!.repos.copy(
                            second = game.value!!.repos.second.copy(
                                starAmount = secondRepoStars,
                                result = RepoDto.Result.CORRECT
                            )
                        ),
                        score = game.value!!.score + 1,
                    )
                    Handler(Looper.getMainLooper()).postDelayed({
                        game.value = game.value!!.copy(
                            firstImage = game.value!!.secondImage,
                            secondImage = nextImage,
                            repos = ReposDto(first = newFirstRepo, second = nextRepo),
                        )
                    }, CORRECT_DELAY)
                }

                RepoDto.Result.WRONG -> {
                    game.value = game.value!!.copy(
                        repos = game.value!!.repos.copy(
                            second = game.value!!.repos.second.copy(
                                starAmount = secondRepoStars,
                                result = RepoDto.Result.WRONG
                            )
                        ),
                    )
                }
            }

            return result
        }
    }
}