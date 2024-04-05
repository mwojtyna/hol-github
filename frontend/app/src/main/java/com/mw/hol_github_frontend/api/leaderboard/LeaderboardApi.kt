package com.mw.hol_github_frontend.api.leaderboard

import retrofit2.http.GET

interface LeaderboardApi {
    @GET("leaderboard")
    suspend fun getLeaderboard(): Array<LeaderboardEntry>
}
