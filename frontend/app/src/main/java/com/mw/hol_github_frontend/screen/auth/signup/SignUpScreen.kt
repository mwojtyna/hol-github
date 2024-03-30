package com.mw.hol_github_frontend.screen.auth.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.mw.hol_github_frontend.LocalErrorSnackbar
import com.mw.hol_github_frontend.R
import com.mw.hol_github_frontend.api.ApiClient
import com.mw.hol_github_frontend.composable.PasswordField
import com.mw.hol_github_frontend.composable.Spinner
import com.mw.hol_github_frontend.theme.AppTheme
import com.mw.hol_github_frontend.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    navigateToSignIn: () -> Unit,
    onSignUp: () -> Unit,
) {
    val username by viewModel.username.collectAsState()
    val usernameError by viewModel.usernameError.collectAsState()

    val password by viewModel.password.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()

    val repeatedPassword by viewModel.repeatedPassword.collectAsState()
    val repeatedPasswordError by viewModel.repeatedPasswordError.collectAsState()

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var repeatedPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var loading by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val errorSnackbar = LocalErrorSnackbar.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    fun signUp() {
        viewModel.viewModelScope.launch {
            if (!viewModel.validate()) {
                return@launch
            }

            loading = true
            val res = viewModel.signUp(username, password)
            loading = false

            if (res.isSuccessful) {
                onSignUp()
            } else if (res.code() == 409) {
                errorSnackbar.showSnackbar(context.getString(R.string.signup_error))
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(56.dp)
        ) {
            Text(
                stringResource(R.string.signup_title),
                style = Typography.headlineLarge,
                textAlign = TextAlign.Center,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { viewModel.setUsername(it) },
                    label = { Text(stringResource(R.string.signup_username_label)) },
                    supportingText = fun(): @Composable (() -> Unit)? {
                        return if (usernameError.isNotBlank()) {
                            { Text(usernameError) }
                        } else {
                            null
                        }
                    }(),
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.AccountCircle,
                            stringResource(R.string.signup_username_label)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                )

                PasswordField(
                    password = password,
                    onPasswordChange = viewModel::setPassword,
                    label = stringResource(R.string.signup_password_label),
                    supportingText = passwordError,
                    isVisible = passwordVisible,
                    onVisibilityChange = { passwordVisible = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                )

                PasswordField(
                    password = repeatedPassword,
                    onPasswordChange = viewModel::setRepeatedPassword,
                    label = stringResource(R.string.signup_repeated_password_label),
                    supportingText = repeatedPasswordError,
                    isVisible = repeatedPasswordVisible,
                    onVisibilityChange = { repeatedPasswordVisible = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        signUp()
                        keyboardController?.hide()
                    })
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = { signUp() },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            10.dp, Alignment.CenterHorizontally
                        ),
                    ) {
                        Text(
                            stringResource(R.string.signup_title),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 5.dp)
                        )

                        if (loading) {
                            Spinner()
                        }
                    }
                }

                TextButton(onClick = navigateToSignIn) {
                    Text(
                        stringResource(R.string.signup_signin_button),
                        textAlign = TextAlign.Center,
                        style = Typography.labelMedium,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SignUpPreview() {
    val context = LocalContext.current

    AppTheme(useDarkTheme = true) {
        SignUpScreen(viewModel = SignUpViewModel(
            context = context, apiClient = ApiClient(context)
        ), navigateToSignIn = {}, onSignUp = {})
    }
}