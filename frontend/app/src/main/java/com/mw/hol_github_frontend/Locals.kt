package com.mw.hol_github_frontend

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalErrorSnackbar: ProvidableCompositionLocal<SnackbarHostState> =
    compositionLocalOf { null!! }

val LocalNavController: ProvidableCompositionLocal<NavHostController> =
    compositionLocalOf { null!! }