package com.keru.novelly.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.keru.novelly.data.data_source.local.models.Book
import com.keru.novelly.data.data_source.local.models.genres
import com.keru.novelly.domain.repositories.BooksRepository
import com.keru.novelly.utils.FirebasePaths
import com.keru.novelly.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : BooksRepository {

    override suspend fun getRandomBooks(): Flow<Resource<List<Book>>> = flow {
        var retryCount = 0
        val maxRetries = 5 // Set the maximum number of retries

        while (retryCount < maxRetries) {
            try {
                val snapshot = fireStore.collection(FirebasePaths.Books.path)
                    .whereEqualTo("category", genres.first())
                    .limit(25)
                    .get()
                    .await()

                snapshot.runCatching {

                }.onFailure {

                }

                val books = snapshot.toObjects(Book::class.java).shuffled()
                if (!snapshot.isEmpty) {
                    emit(Resource.Success(data = books))
                    return@flow // Exit the loop if the snapshot is not empty
                } else {
                    retryCount++
                    delay(1000) // Add a delay before retrying (adjust the duration as needed)
                }
            } catch (e: Exception) {
                emit(Resource.Error(message = e.message ?: "An unknown error occurred!"))
                return@flow // Exit the loop in case of an exception
            }
        }

        // If the loop completes without a non-empty snapshot, emit an empty list
        emit(Resource.Success(data = emptyList()))
    }

    override suspend fun getPopularBooks(): Flow<Resource<List<Book>>> = flow {
        try {
            val snapshot = fireStore.collection(FirebasePaths.Books.path).orderBy(
                "downloads", Query.Direction.DESCENDING
            ).limit(50).get().await()
            val books = snapshot.toObjects(Book::class.java)
            Log.d("Search", "Popular results ==> ${books.size}")

            if (!snapshot.isEmpty) {
                emit(
                    Resource.Success(
                        data = books.shuffled().take(5).sortedByDescending { it.downloads })
                )
            } else {
                emit(Resource.Error(message = "Document snapshot is empty!"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: "An unknown error occurred!"))
        }
    }

    override suspend fun searchBooks(query: String): Flow<Resource<List<Book>>> =
        flow<Resource<List<Book>>> {
            val queries = query.lowercase(Locale.getDefault()).split(" ")

            val snapshot = fireStore.collection(FirebasePaths.Books.path)
                .whereArrayContains("searchTerms", query.trim().lowercase(Locale.getDefault()))
                //.whereIn("searchTerms", queries)
                .get().await()

            val books = snapshot.toObjects(Book::class.java)
            Log.d("Search", "Query results ==> ${books.size}")
            emit(Resource.Success(data = books))
        }.catch {
            //emit(Resource.Error(message = it.message ?: "An unknown error occurred!"))
            Log.d("Search", "Error ==> ${it}")
        }

    override suspend fun getBookDetails(id: String): Flow<Resource<Book>> {

        val value = MutableStateFlow<Resource<Book>>(Resource.Success(data = Book()))
        Log.d("BookTest", "Getting Book Details ID ==> $id")
        try {
            val path = FirebasePaths.Books.path
            fireStore.collection(path).document(id)
                .addSnapshotListener { snapshot, error ->
                    snapshot?.let {
                        value.tryEmit(Resource.Success(data = it.toObject(Book::class.java)))
                    }
                    error?.message?.let {
                        value.tryEmit(Resource.Error(message = it))
                    }
                }
        } catch (e: FirebaseFirestoreException) {
            Log.d("BookTest", "Error ==> $e")
            value.tryEmit(
                Resource.Error(message = e.message ?: "An unknown error occurred!")
            )
        }

        return value
    }

    override suspend fun incrementDownloadCount(id: String) {
        val path = FirebasePaths.Books.path
        fireStore.collection(path).document(id).update(
            "downloads", FieldValue.increment(1)
        ).await()
    }

    override suspend fun getCompletedBooks(names: List<String>): Flow<Resource<List<Book>>> = flow {
        val path = FirebasePaths.Books.path
        if (names.isNotEmpty()) {
            val snapshots = fireStore.collection(path).whereIn("title", names).get().await()
            if (!snapshots.isEmpty) {
                emit(Resource.Success(data = snapshots.toObjects(Book::class.java)))
            } else {
                emit(Resource.Success(data = emptyList()))
            }
        } else {
            emit(Resource.Success(data = emptyList()))
        }
    }

    override suspend fun saveBook(book: Book): Result<String> {
        return try {
            val booksCollection = FirebasePaths.Books.path
            fireStore.collection(booksCollection).document(book.bid).set(book).await()
            Result.success("Book uploaded!")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadBook(bookUri: Uri, userId: String): Result<String> {
        val path = "Books/" + userId + System.currentTimeMillis().toString()
        val ref = storage.getReference(path)
        return try {
            ref.putFile(bookUri).await()
            val downloadUrl = ref.downloadUrl.await()
            Log.d("books", "Uri: $downloadUrl")
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Log.d("books", "Error: ${e.message}")
            Result.failure(e)
        }

    }

    override suspend fun uploadBookCover(imageUri: Uri, bookTitle: String): Result<String> {
        val path = "Covers/" + bookTitle + System.currentTimeMillis().toString()
        val ref = storage.getReference(path)

        return try {
            ref.putFile(imageUri).await()
            val downloadUri = ref.downloadUrl.await()
            Log.d("books", "Uri: $downloadUri")
            Result.success(downloadUri.toString())
        } catch (e: Exception) {
            Log.d("books", "Error: ${e.message}")
            Result.failure(e)
        }
    }
}