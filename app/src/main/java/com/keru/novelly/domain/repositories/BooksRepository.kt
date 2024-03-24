package com.keru.novelly.domain.repositories

import android.net.Uri
import com.keru.novelly.data.data_source.local.models.Book
import com.keru.novelly.utils.Resource
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    suspend fun getRandomBooks(): Flow<Resource<List<Book>>>

    suspend fun getPopularBooks(): Flow<Resource<List<Book>>>

    suspend fun searchBooks(query: String): Flow<Resource<List<Book>>>

    suspend fun getBookDetails(id: String): Flow<Resource<Book>>

    suspend fun incrementDownloadCount(id: String)

    suspend fun getCompletedBooks(names: List<String>): Flow<Resource<List<Book>>>


    suspend fun saveBook(book: Book): Result<String>
    suspend fun uploadBook(bookUri: Uri, userId: String): Result<String>

    suspend fun uploadBookCover(imageUri: Uri, bookTitle: String): Result<String>

}