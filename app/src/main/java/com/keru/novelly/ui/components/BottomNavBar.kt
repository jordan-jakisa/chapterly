package com.keru.novelly.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalLibrary
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.keru.novelly.ui.navigation.Routes

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Search,
    BottomNavItem.MyLibrary,
    BottomNavItem.More
)

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar {
        bottomNavItems.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = {
                    Text(text = item.label)
                }
            )
        }
    }
}

sealed class BottomNavItem(
    val route: String, val icon: ImageVector, val label: String
) {
    data object Home : BottomNavItem(
        route = Routes.Home.path,
        icon = Icons.Rounded.Home,
        label = "Home"
    )

    data object Search : BottomNavItem(
        route = Routes.Search.path,
        icon = Icons.Rounded.Explore,
        label = "Explore"
    )

    data object MyLibrary : BottomNavItem(
        route = Routes.Downloads.path,
        icon = Icons.Rounded.LocalLibrary,
        label = "My Library"
    )

    data object More : BottomNavItem(
        route = Routes.More.path,
        icon = Icons.Rounded.Menu,
        label = "More"
    )

}