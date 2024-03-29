package com.mw.hol_github_frontend.screen.main.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.mw.hol_github_frontend.R
import com.mw.hol_github_frontend.api.ApiClient
import com.mw.hol_github_frontend.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun UserScreen(
    viewModel: UserViewModel = hiltViewModel(),
    navigateToSignIn: () -> Unit,
) {
    val username by viewModel.username.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUserData()
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Card(modifier = Modifier.wrapContentSize()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Outlined.AccountCircle, "username")
                    Text(username)
                }

                Button(onClick = {
                    viewModel.viewModelScope.launch {
                        viewModel.signOut()
                        navigateToSignIn()
                    }
                }) {
                    Text(
                        stringResource(R.string.user_signout_button),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun UserScreenPreview() {
    AppTheme(useDarkTheme = true) {
        UserScreen(
            navigateToSignIn = {}, viewModel = UserViewModel(ApiClient(LocalContext.current))
        )
    }
}