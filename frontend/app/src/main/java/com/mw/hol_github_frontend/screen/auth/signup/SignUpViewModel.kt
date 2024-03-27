package com.mw.hol_github_frontend.screen.auth.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mw.hol_github_frontend.R
import com.mw.hol_github_frontend.api.ApiClient
import com.mw.hol_github_frontend.api.ApiUserSignupRequest
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response

class SignUpViewModel(
    private val app: Application,
    private val apiClient: ApiClient,
) : AndroidViewModel(app) {
    companion object {
        private val PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\w\\W]{8,}\$".toRegex()
    }

    val username = MutableStateFlow("")
    val usernameError = MutableStateFlow("")
    fun setUsername(username: String) {
        this.username.value = username
        validateUsername()
    }

    val password = MutableStateFlow("")
    val passwordError = MutableStateFlow("")
    fun setPassword(password: String) {
        this.password.value = password
        validatePassword()
        validateRepeatedPassword()
    }

    val repeatedPassword = MutableStateFlow("")
    val repeatedPasswordError = MutableStateFlow("")
    fun setRepeatedPassword(repeatedPassword: String) {
        this.repeatedPassword.value = repeatedPassword
        validateRepeatedPassword()
    }

    suspend fun signUp(
        username: String,
        password: String,
    ): Response<Unit> {
        return apiClient.user.signUp(ApiUserSignupRequest(username, password))
    }

    fun validate(): Boolean {
        // Don't inline these calls, because otherwise when one of them is false, the other ones won't be called
        val usernameValid = validateUsername()
        val passwordValid = validatePassword()
        val repeatedPasswordValid = validateRepeatedPassword()
        return usernameValid && passwordValid && repeatedPasswordValid
    }

    private fun validateUsername(): Boolean {
        return if (username.value.isBlank()) {
            usernameError.value = app.getString(R.string.signup_username_error)
            false
        } else {
            usernameError.value = ""
            true
        }
    }

    private fun validatePassword(): Boolean {
        return if (!PASSWORD_REGEX.matches(password.value)) {
            passwordError.value = app.getString(R.string.signup_password_error)
            false
        } else {
            passwordError.value = ""
            true
        }
    }

    private fun validateRepeatedPassword(): Boolean {
        return if (password.value != repeatedPassword.value) {
            repeatedPasswordError.value =
                app.getString(R.string.signup_repeated_password_error)
            false
        } else {
            repeatedPasswordError.value = ""
            true
        }
    }
}