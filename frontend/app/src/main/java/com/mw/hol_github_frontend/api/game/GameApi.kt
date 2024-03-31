package com.mw.hol_github_frontend.api.game

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface GameApi {
    @POST("game/new")
    suspend fun new(): ResponseBody

    @POST("game/choose")
    suspend fun choose(@Body body: ApiGameChooseRequest): ResponseBody
}