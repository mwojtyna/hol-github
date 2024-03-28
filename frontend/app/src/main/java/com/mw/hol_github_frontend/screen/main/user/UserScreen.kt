package com.mw.hol_github_frontend.screen.main.user

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.mw.hol_github_frontend.api.ApiClient
import com.mw.hol_github_frontend.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun UserScreen(
    apiClient: ApiClient,
    viewModel: UserViewModel = UserViewModel(apiClient),
    navigateToSignIn: () -> Unit,
) {
    val username by rememberSaveable { viewModel.username }

    LaunchedEffect(Unit) {
        viewModel.fetchUserData()
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(username)

            Button(onClick = {
                viewModel.viewModelScope.launch {
                    viewModel.signOut()
                    navigateToSignIn()
                }
            }) {
                Text("Sign Out")
            }
        }
    }
}

@Composable
@Preview
fun UserScreenPreview() {
    AppTheme(useDarkTheme = true) {
        UserScreen(
            apiClient = ApiClient(LocalContext.current.applicationContext as Application),
        ) {}
    }
}