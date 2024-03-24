package com.mw.hol_github_frontend.composable

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mw.hol_github_frontend.api.ApiClient
import com.mw.hol_github_frontend.screen.signin.SignInScreen
import com.mw.hol_github_frontend.screen.signup.SignUpScreen

const val DURATION = 300

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val apiClient = ApiClient(LocalContext.current)

    NavHost(
        navController = navController,
        startDestination = "signup",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(DURATION)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(DURATION)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(DURATION)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(DURATION)
            )
        },
    ) {
        composable("signup") {
            SignUpScreen(apiClient = apiClient,
                navigateToSignIn = { navController.navigate("signin") })
        }
        composable("signin") {
            SignInScreen(
                apiClient = apiClient,
                navigateToSignUp = { navController.navigate("signup") },
            )
        }
    }
}