package com.mw.hol_github_frontend.screen.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.mw.hol_github_frontend.R
import com.mw.hol_github_frontend.api.ApiClient
import com.mw.hol_github_frontend.composable.PasswordField
import com.mw.hol_github_frontend.theme.AppTheme
import com.mw.hol_github_frontend.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    apiClient: ApiClient,
    viewModel: SignInViewModel = SignInViewModel(apiClient),
    focusManager: FocusManager = LocalFocusManager.current,
    navigateToSignUp: () -> Unit,
) {
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(56.dp)
        ) {
            Text(
                "Sign in",
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
                    isVisible = passwordVisible,
                    onVisibilityChange = { passwordVisible = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = {
                        viewModel.viewModelScope.launch {
                            viewModel.signIn(username, password)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        "Sign in",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }

                TextButton(onClick = navigateToSignUp) {
                    Text(
                        "Don't have an account yet?",
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
fun Preview() {
    AppTheme(useDarkTheme = true) {
        SignInScreen(apiClient = ApiClient(LocalContext.current), navigateToSignUp = {})
    }
}