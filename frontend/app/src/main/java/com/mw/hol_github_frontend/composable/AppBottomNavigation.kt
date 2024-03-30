package com.mw.hol_github_frontend.composable

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Gamepad
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.mw.hol_github_frontend.LocalNavController
import com.mw.hol_github_frontend.theme.AppTheme

sealed class NavTarget(val route: String) {
    data object User : NavTarget("user")
    data object Game : NavTarget("gamestart")
    data object Leaderboard : NavTarget("leaderboard")
}

class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val iconSelected: ImageVector,
    val screen: NavTarget,
)

@Composable
fun AppBottomNavigation() {
    val items = listOf(
        NavigationItem(
            "User",
            icon = Icons.Outlined.AccountCircle,
            iconSelected = Icons.Filled.AccountCircle,
            screen = NavTarget.User
        ),
        NavigationItem(
            "Game",
            icon = Icons.Outlined.Gamepad,
            iconSelected = Icons.Filled.Gamepad,
            screen = NavTarget.Game
        ),
        NavigationItem(
            "Leaderboard",
            icon = Icons.Outlined.Leaderboard,
            iconSelected = Icons.Filled.Leaderboard,
            screen = NavTarget.Leaderboard
        ),
    )
    val navController = LocalNavController.current
    val backStack by LocalNavController.current.currentBackStackEntryFlow.collectAsState(initial = null)
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(-1) }

    DisposableEffect(navController) {
        val onRouteChangeListener: (controller: NavController, destination: NavDestination, arguments: Bundle?) -> Unit =
            { _, destination, _ ->
                selectedItemIndex = items.indexOfFirst { it.screen.route == destination.route }
            }

        navController.addOnDestinationChangedListener(onRouteChangeListener)

        onDispose {
            navController.removeOnDestinationChangedListener(onRouteChangeListener)
        }
    }

    if (!items.any { it.screen.route == backStack?.destination?.route }) {
        return
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            val selected = index == selectedItemIndex
            NavigationBarItem(selected = selected, label = { Text(item.title) }, icon = {
                Icon(if (selected) item.iconSelected else item.icon, item.title)
            }, onClick = {
                navController.navigate(item.screen.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
            })
        }
    }
}

@Preview
@Composable
private fun AppBottomNavigationPreview() {
    AppTheme(useDarkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            AppScaffold {}
        }
    }
}
