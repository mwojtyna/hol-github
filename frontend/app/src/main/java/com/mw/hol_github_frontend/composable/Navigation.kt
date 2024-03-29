package com.mw.hol_github_frontend.composable

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mw.hol_github_frontend.LocalNavController
import com.mw.hol_github_frontend.api.ApiClient
import com.mw.hol_github_frontend.screen.auth.signin.SignInScreen
import com.mw.hol_github_frontend.screen.auth.signup.SignUpScreen
import com.mw.hol_github_frontend.screen.main.game.GameScreen
import com.mw.hol_github_frontend.screen.main.leaderboard.LeaderboardScreen
import com.mw.hol_github_frontend.screen.main.user.UserScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

const val DURATION = 300
const val SCALE = 0.95f

@Composable
fun Navigation(
    viewModel: NavigationViewModel = hiltViewModel(),
) {
    val navController = LocalNavController.current
    val apiClient = viewModel.apiClient

    fun afterAuth() {
        onMainThread {
            navController.popBackStack("auth", inclusive = true)
            navController.navigate("main")
        }
    }

    AppScaffold {
        NavHost(
            navController = navController,
            startDestination = runBlocking {
                if (apiClient.user.me().isSuccessful) "main" else "auth"
            },
        ) {
            navigation(
                route = "auth", startDestination = "signin",
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
                composable("signup") {
                    SignUpScreen(navigateToSignIn = { onMainThread { navController.navigate("signin") } },
                        onSignUp = { afterAuth() })
                }
                composable("signin") {
                    SignInScreen(navigateToSignUp = { onMainThread { navController.navigate("signup") } },
                        onSignIn = { afterAuth() })
                }
            }

            navigation(
                route = "main", startDestination = "game",
                enterTransition = {
                    fadeIn(tween(DURATION)) + scaleIn(
                        tween(DURATION),
                        initialScale = SCALE
                    )
                },
                exitTransition = {
                    fadeOut(tween(DURATION)) + scaleOut(
                        tween(DURATION),
                        targetScale = SCALE
                    )
                },
                popEnterTransition = {
                    fadeIn(tween(DURATION)) + scaleIn(
                        tween(DURATION),
                        initialScale = SCALE
                    )
                },
                popExitTransition = {
                    fadeOut(tween(DURATION)) + scaleOut(
                        tween(DURATION),
                        targetScale = SCALE
                    )
                },
            ) {
                composable("user") {
                    UserScreen(navigateToSignIn = {
                        onMainThread {
                            navController.popBackStack("main", inclusive = true)
                            navController.navigate("auth")
                        }
                    })
                }

                composable("game") {
                    GameScreen()
                }

                composable("leaderboard") {
                    LeaderboardScreen()
                }
            }
        }
    }
}

private fun onMainThread(cb: () -> Unit) {
    val mainHandler = Handler(Looper.getMainLooper())
    val runnable = Runnable {
        cb()
    }
    mainHandler.post(runnable)
}

@HiltViewModel
class NavigationViewModel @Inject constructor(val apiClient: ApiClient) : ViewModel()