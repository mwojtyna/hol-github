package com.mw.hol_github_frontend.api.game

import com.mw.hol_github_frontend.api.user.ApiUserSigninRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GameApi {
    @POST("game/new")
    suspend fun new(): ResponseBody

    @POST("user/choose")
    suspend fun choose(
        @Body body: ApiUserSigninRequest,
    ): Response<Unit>
}