package com.mw.hol_github_frontend

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf

val LocalErrorSnackbar = compositionLocalOf { SnackbarHostState() }