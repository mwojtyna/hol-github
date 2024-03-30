package com.mw.hol_github_frontend.screen.main.gamestart

import androidx.lifecycle.ViewModel
import com.mw.hol_github_frontend.api.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class GameStartViewModel @Inject constructor(
    private val apiClient: ApiClient,
) :
    ViewModel() {
    val highscore = MutableStateFlow(0)

    suspend fun fetchHighscore() {
        highscore.value = apiClient.user.me().body()!!.highscore
    }
}

