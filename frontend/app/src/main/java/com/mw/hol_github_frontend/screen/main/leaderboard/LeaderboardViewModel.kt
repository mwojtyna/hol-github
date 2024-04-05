package com.mw.hol_github_frontend.screen.main.leaderboard

import androidx.lifecycle.ViewModel
import com.mw.hol_github_frontend.api.ApiClient
import com.mw.hol_github_frontend.api.leaderboard.LeaderboardEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(private val apiClient: ApiClient) : ViewModel() {
    val leaderboardData = MutableStateFlow<Array<LeaderboardEntry>>(emptyArray())

    suspend fun fetchLeaderboard() {
        leaderboardData.value = apiClient.leaderboard.getLeaderboard()
    }
}