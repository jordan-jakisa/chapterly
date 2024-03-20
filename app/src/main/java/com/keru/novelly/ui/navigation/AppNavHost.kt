package com.keru.novelly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.keru.novelly.ui.pages.about_app.AboutAppScreen
import com.keru.novelly.ui.pages.auth.LoginPage
import com.keru.novelly.ui.pages.auth.RecoverPasswordScreen
import com.keru.novelly.ui.pages.auth.SignupPage
import com.keru.novelly.ui.pages.book_details.BookDetailsPage
import com.keru.novelly.ui.pages.completed_books.CompletedBooksScree
import com.keru.novelly.ui.pages.downloads.DownloadsPage
import com.keru.novelly.ui.pages.home.HomePage
import com.keru.novelly.ui.pages.more_page.MorePage
import com.keru.novelly.ui.pages.search.SearchPage
import com.keru.novelly.ui.pages.settings_page.SettingsScreen
import com.keru.novelly.ui.pages.uploadpage.UploadBookScreen
import com.keru.novelly.ui.pages.user_profile_page.UserScreen

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.path
    ) {

        composable(Routes.Home.path) {
            HomePage(navController)
        }

        composable(Routes.Search.path) {
            SearchPage(navController)
        }

        composable(Routes.More.path) {
            MorePage(navController)
        }

        composable(Routes.Downloads.path) {
            DownloadsPage(navController)
        }

        composable(
            route = Routes.BookDetails.path,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("bookId")
            BookDetailsPage(bookId = id, navController = navController)
        }

        composable(Routes.LoginPage.path) {
            LoginPage(navController = navController)
        }

        composable(Routes.RecoverPasswordPage.path) {
            RecoverPasswordScreen(navController = navController)
        }

        composable(Routes.RegisterPage.path) {
            SignupPage(navController = navController)
        }

        composable(Routes.AboutAppPage.path) {
            AboutAppScreen(navController = navController)
        }

        composable(Routes.SettingsPage.path) {
            SettingsScreen(navController = navController)
        }

        composable(Routes.CompletedBooksPage.path) {
            CompletedBooksScree(navController = navController)
        }
        composable(Routes.UserPage.path) {
            UserScreen(navController = navController)
        }

        composable(Routes.UploadBookPage.path) {
            UploadBookScreen(navController = navController)
        }

    }
}