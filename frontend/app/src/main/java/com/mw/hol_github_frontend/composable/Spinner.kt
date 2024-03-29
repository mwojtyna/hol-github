package com.mw.hol_github_frontend.composable

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mw.hol_github_frontend.theme.AppTheme

@Composable
fun Spinner(size: Dp = 20.dp, strokeWidth: Dp = 3.dp) {
    CircularProgressIndicator(
        modifier = Modifier.size(size),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        strokeWidth = strokeWidth,
    )
}

@Preview
@Composable
private fun SpinnerPreview() {
    AppTheme {
        Spinner()
    }
}