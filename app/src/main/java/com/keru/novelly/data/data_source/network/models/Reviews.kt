package com.keru.novelly.data.data_source.network.models

data class Reviews(
    val id: String,
    val bookId: String,
    val rating: Float,
    val message: String,
    val userId: String
)