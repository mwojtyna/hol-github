package com.mw.hol_github_frontend.screen.main.user

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mw.hol_github_frontend.api.ApiClient

class UserViewModel(private val apiClient: ApiClient) : ViewModel() {
    val username = mutableStateOf("")

    suspend fun fetchUserData(): Boolean {
        val res = apiClient.user.me()
        if (!res.isSuccessful) {
            return false
        }
        username.value = res.body()!!.username
        return true
    }

    suspend fun signOut() {
        apiClient.user.signOut()
    }
}
