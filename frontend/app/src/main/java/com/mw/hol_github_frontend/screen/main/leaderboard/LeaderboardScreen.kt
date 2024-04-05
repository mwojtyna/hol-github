package com.mw.hol_github_frontend.screen.main.leaderboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mw.hol_github_frontend.theme.AppTheme

@Composable
fun LeaderboardScreen(viewModel: LeaderboardViewModel = hiltViewModel()) {
    val leaderboardData by viewModel.leaderboardData.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchLeaderboard()
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(items = leaderboardData) { index, it ->
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text((index + 1).toString())
                        Icon(Icons.Outlined.AccountCircle, contentDescription = "Profile picture")
                        Text(it.username)
                    }
                    Text(it.score.toString())
                }
                HorizontalDivider()
            }
        }
    }
}

@Preview
@Composable
private fun LeaderboardScreenPreview() {
    AppTheme(useDarkTheme = true) {
        LeaderboardScreen()
    }
}