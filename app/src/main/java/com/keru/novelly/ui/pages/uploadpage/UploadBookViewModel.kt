package com.keru.novelly.ui.pages.uploadpage

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.keru.novelly.data.data_source.local.models.Book
import com.keru.novelly.domain.repositories.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadBookViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    var uiState by mutableStateOf(UploadBookUiState())
        private set

    fun uploadBook(
        mBook: Book,
        bookUri: Uri,
        imageUri: Uri,
        onComplete: () -> Unit
    ) {
        uiState = uiState.copy(
            isLoading = true
        )


        val book =
            mBook.copy(
                bid = System.currentTimeMillis().toString() + (auth.currentUser?.uid ?: "nullUser"),
                searchTerms = generateSearchTerms(mBook.title, mBook.authorName, mBook.category)
            )
        viewModelScope.launch {
            val imageLink = if (imageUri.toString().isNotEmpty()) {
                booksRepository.uploadBookCover(imageUri, book.title).getOrNull()
            } else null

            val bookLink = if (bookUri.toString().isNotEmpty()) booksRepository.uploadBook(
                bookUri,
                auth.currentUser?.uid ?: "noUser"
            ).getOrNull() else null

            val newBook = book.copy(
                image = imageLink ?: "",
                book = bookLink ?: ""
            )

            val result = booksRepository.saveBook(newBook)
            result.onSuccess {
                uiState = uiState.copy(
                    isLoading = false,
                    message = it
                )

                onComplete()
            }

            result.onFailure {
                uiState = uiState.copy(
                    isLoading = false,
                    error = it.message
                )
            }
        }
    }

    private fun generateSearchTerms(title: String, author: String, genre: String): List<String> {
        val key = "$title $author $genre".lowercase()
        return key.split(" ")
    }

}

data class UploadBookUiState(
    var error: String? = "",
    val isLoading: Boolean = false,
    val message: String? = null
)