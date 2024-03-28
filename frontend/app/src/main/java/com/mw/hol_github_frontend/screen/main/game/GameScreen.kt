package com.mw.hol_github_frontend.screen.main.game

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
fun GameScreen() {
    AppScaffold(bottomNav = { AppBottomNavigation(NavTarget.Game) }) {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            Text("Game")
        }
    }
}

@Preview
@Composable
private fun GameScreenPreview() {
    AppTheme(useDarkTheme = true) {
        GameScreen()
    }
}