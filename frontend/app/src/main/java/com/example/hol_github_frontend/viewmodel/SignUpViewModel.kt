package com.example.hol_github_frontend.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SignUpViewModel : ViewModel() {
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
}