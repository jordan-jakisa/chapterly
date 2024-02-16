package com.keru.novelly.data.data_source.network.models

data class User(
    var uid: String? = null,
    var name: String? = null,
    var email: String? = null,
    var image: String? = null,
    var completedBooks: List<String> = emptyList(),
    var likedBooks: List<String> = emptyList(),
    var genresDownloaded: List<String> = emptyList()
)

//age
//gender
//1