package com.mw.hol_github_frontend.composable

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.mw.hol_github_frontend.api.ApiClient
import com.mw.hol_github_frontend.screen.auth.signin.SignInScreen
import com.mw.hol_github_frontend.screen.auth.signup.SignUpScreen
import com.mw.hol_github_frontend.screen.main.user.UserScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

const val DURATION = 300

fun onMainThread(cb: () -> Unit) {
    val mainHandler = Handler(Looper.getMainLooper())
    val runnable = Runnable {
        cb()
    }
    mainHandler.post(runnable)
}

@Composable
fun Navigation(
    viewModel: NavigationViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val apiClient = viewModel.apiClient

    fun afterAuth() {
        onMainThread {
            navController.popBackStack("auth", inclusive = true)
            navController.navigate("main")
        }
    }

    NavHost(
        navController = navController,
        startDestination = runBlocking {
            if (apiClient.user.me().isSuccessful) "main" else "auth"
        },
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, tween(DURATION)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left, tween(DURATION)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, tween(DURATION)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right, tween(DURATION)
            )
        },
    ) {
        navigation(route = "auth", startDestination = "signin") {
            composable("signup") {
                SignUpScreen(
                    navigateToSignIn = { onMainThread { navController.navigate("signin") } },
                    onSignUp = { afterAuth() })
            }
            composable("signin") {
                SignInScreen(
                    navigateToSignUp = { onMainThread { navController.navigate("signup") } },
                    onSignIn = { afterAuth() })
            }
        }

        navigation(route = "main", startDestination = "user") {
            composable("user") {
                UserScreen(navigateToSignIn = {
                    onMainThread {
                        navController.popBackStack("main", inclusive = true)
                        navController.navigate("auth")
                    }
                })
            }
        }
    }
}

@HiltViewModel
class NavigationViewModel @Inject constructor(val apiClient: ApiClient) : ViewModel()