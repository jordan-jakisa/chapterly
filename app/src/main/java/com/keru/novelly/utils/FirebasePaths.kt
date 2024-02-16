package com.keru.novelly.utils

sealed class FirebasePaths(val path: String) {
    data object Books : FirebasePaths("books")
    data object Users : FirebasePaths("users")

    data object Reviews : FirebasePaths("reviews")
}