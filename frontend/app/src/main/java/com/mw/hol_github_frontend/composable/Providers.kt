package com.mw.hol_github_frontend.composable

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.mw.hol_github_frontend.LocalErrorSnackbar
import com.mw.hol_github_frontend.LocalNavController

@Composable
fun Providers(children: @Composable () -> Unit) {
    val snackbar = SnackbarHostState()
    val navController = rememberNavController()

    CompositionLocalProvider(LocalErrorSnackbar provides snackbar) {
        CompositionLocalProvider(LocalNavController provides navController) {
            children()
        }
    }
}