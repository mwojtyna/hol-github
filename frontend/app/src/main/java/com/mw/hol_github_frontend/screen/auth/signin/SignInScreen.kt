package com.mw.hol_github_frontend.screen.auth.signin

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
import com.mw.hol_github_frontend.composable.AppScaffold
import com.mw.hol_github_frontend.composable.PasswordField
import com.mw.hol_github_frontend.composable.Spinner
import com.mw.hol_github_frontend.theme.AppTheme
import com.mw.hol_github_frontend.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    navigateToSignUp: () -> Unit,
    onSignIn: () -> Unit,
) {
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var loading by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val errorSnackbar = LocalErrorSnackbar.current
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun signIn() {
        viewModel.viewModelScope.launch {
            loading = true
            val res = viewModel.signIn(username, password)
            loading = false

            if (res.isSuccessful) {
                onSignIn()
            } else {
                errorSnackbar.showSnackbar(context.getString(R.string.signin_error))
            }
        }
    }

    AppScaffold {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(56.dp)
            ) {
                Text(
                    stringResource(R.string.signin_title),
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
                        label = { Text(stringResource(R.string.signin_username_label)) },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.AccountCircle,
                                stringResource(R.string.signin_username_label)
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
                        label = stringResource(R.string.signin_password_label),
                        isVisible = passwordVisible,
                        onVisibilityChange = { passwordVisible = it },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            signIn()
                            keyboardController?.hide()
                        }),
                    )
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(
                        onClick = { signIn() },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                10.dp, Alignment.CenterHorizontally
                            ),
                        ) {
                            Text(
                                stringResource(R.string.signin_button),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 5.dp)
                            )

                            if (loading) {
                                Spinner()
                            }
                        }
                    }

                    TextButton(onClick = navigateToSignUp) {
                        Text(
                            stringResource(R.string.signin_signup_button),
                            textAlign = TextAlign.Center,
                            style = Typography.labelMedium,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    AppTheme(useDarkTheme = true) {
        SignInScreen(viewModel = SignInViewModel(ApiClient(LocalContext.current)),
            navigateToSignUp = {},
            onSignIn = {})
    }
}