package com.keru.novelly.ui.navigation

sealed class Routes(val path: String) {

    data object Home : Routes("home_page")
    data object Search : Routes("search_page")
    data object Downloads : Routes("downloads_page")
    data object More : Routes("more_page")

    data object BookDetails : Routes("book_details/{bookId}")

    data object LoginPage : Routes("login_page")
    data object RegisterPage : Routes("signup_page")
    data object RecoverPasswordPage : Routes("recover_password_page")
    data object AboutAppPage : Routes("about_app_page")
    data object SettingsPage : Routes("settings_page")
    data object CompletedBooksPage : Routes("completed_books_page")
    data object UserPage : Routes("user_page")
    data object UploadBookPage : Routes("upload_book_page")




}