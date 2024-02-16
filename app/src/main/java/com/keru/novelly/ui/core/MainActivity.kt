package com.keru.novelly.ui.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.keru.novelly.data.schedulers.StreakWorker
import com.keru.novelly.ui.components.BottomNavBar
import com.keru.novelly.ui.components.bottomNavItems
import com.keru.novelly.ui.navigation.AppNavHost
import com.keru.novelly.ui.theme.NovellyTheme
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var jsonObject: JSONObject
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppScreen()
        }

        val workRequest = PeriodicWorkRequestBuilder<StreakWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "StreakWorker",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )

    }
}

@Composable
fun AppScreen(
    vm: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NovellyTheme(
        darkTheme = vm.uiState.isDarkMode,
        dynamicColor = vm.uiState.isDynamicColor
    ) {
        // A surface container using the 'background' color from the theme

        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible =
                    bottomNavItems.map { it.route }
                        .contains(currentDestination?.route)
                ) {
                    BottomNavBar(navController)
                }
            }
        ) { paddingValues ->
            val padding = paddingValues
            AppNavHost(navController)
        }
    }
}