package com.mw.hol_github_frontend.composable

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.mw.hol_github_frontend.LocalErrorSnackbar

@Composable
fun Providers(children: @Composable () -> Unit) {
    val snackbar = SnackbarHostState()
    CompositionLocalProvider(LocalErrorSnackbar provides snackbar) {
        children()
    }
}