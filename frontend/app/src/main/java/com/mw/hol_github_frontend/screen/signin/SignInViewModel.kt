package com.mw.hol_github_frontend.screen.signin

import androidx.lifecycle.ViewModel
import com.mw.hol_github_frontend.api.ApiClient
import com.mw.hol_github_frontend.api.ApiUserSigninRequest
import kotlinx.coroutines.flow.MutableStateFlow

class SignInViewModel(private val apiClient: ApiClient) : ViewModel() {
    val username = MutableStateFlow("")
    fun setUsername(username: String) {
        this.username.value = username
    }

    val password = MutableStateFlow("")
    fun setPassword(password: String) {
        this.password.value = password
    }

    suspend fun signIn(username: String, password: String) {
        apiClient.user.signIn(ApiUserSigninRequest(username, password))
    }
}