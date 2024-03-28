package com.mw.hol_github_frontend.screen.main.leaderboard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mw.hol_github_frontend.composable.AppBottomNavigation
import com.mw.hol_github_frontend.composable.AppScaffold
import com.mw.hol_github_frontend.composable.NavTarget
import com.mw.hol_github_frontend.theme.AppTheme

@Composable
fun LeaderboardScreen() {
    AppScaffold(bottomNav = { AppBottomNavigation(NavTarget.Leaderboard) }) {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            Text("Leaderboard")
        }
    }
}

@Preview
@Composable
private fun GameScreenPreview() {
    AppTheme(useDarkTheme = true) {
        LeaderboardScreen()
    }
}