package com.mw.hol_github_frontend.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mw.hol_github_frontend.LocalErrorSnackbar

@Composable
fun AppScaffold(children: @Composable () -> Unit) {
    val errorSnackbar = LocalErrorSnackbar.current

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = errorSnackbar) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.error
            )
        }
    }) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            children()
        }
    }
}