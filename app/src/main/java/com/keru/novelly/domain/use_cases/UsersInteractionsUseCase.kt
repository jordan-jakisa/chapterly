package com.keru.novelly.domain.use_cases

import com.keru.novelly.utils.Resource

interface UsersInteractionsUseCase {
    suspend fun hasUserLikedBook(bookId: String): Boolean

    suspend fun likeBook(bookId: String): Resource<Boolean>

    suspend fun disLikeBook(bookId: String): Resource<Boolean>
}