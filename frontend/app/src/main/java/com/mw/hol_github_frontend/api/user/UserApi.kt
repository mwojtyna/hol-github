package com.mw.hol_github_frontend.api.user

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    @POST("user/signup")
    suspend fun signUp(
        @Body body: ApiUserSignupRequest,
    ): Response<Unit>

    @POST("user/signin")
    suspend fun signIn(
        @Body body: ApiUserSigninRequest,
    ): Response<Unit>

    @POST("user/signout")
    suspend fun signOut(): Response<Unit>

    @GET("user/me")
    suspend fun me(): Response<ApiUserMeResponse>
}