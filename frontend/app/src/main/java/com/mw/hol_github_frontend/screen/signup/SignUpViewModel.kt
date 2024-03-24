package com.mw.hol_github_frontend.screen.signup

import androidx.lifecycle.ViewModel
import com.mw.hol_github_frontend.api.ApiClient
import com.mw.hol_github_frontend.api.ApiUserSignupRequest
import kotlinx.coroutines.flow.MutableStateFlow

class SignUpViewModel(private val apiClient: ApiClient) : ViewModel() {
    val username = MutableStateFlow("")
    fun setUsername(username: String) {
        this.username.value = username
    }

    val password = MutableStateFlow("")
    fun setPassword(password: String) {
        this.password.value = password
    }

    val repeatedPassword = MutableStateFlow("")
    fun setRepeatedPassword(repeatedPassword: String) {
        this.repeatedPassword.value = repeatedPassword
    }

    suspend fun signUp(username: String, password: String, repeatedPassword: String) {
        apiClient.user.signUp(ApiUserSignupRequest(username, password))
    }
}