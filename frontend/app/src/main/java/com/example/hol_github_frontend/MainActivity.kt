package com.example.hol_github_frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.hol_github_frontend.screen.signup.SignUpScreen
import com.example.hol_github_frontend.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // Change to home screen
                SignUpScreen()
            }
        }
    }
}