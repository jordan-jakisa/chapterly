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
        title: String,
        description: String,
        author: String,
        genre: String,
        imageUri: Uri?,
        bookRating: Float
    ) {
        val book = Book(
            bid = System.currentTimeMillis().toString() + (auth.currentUser?.uid ?: "nullUser"),
            title = title,
            description = description,
            authorName = author,
            category = genre,
            rating = bookRating.toFloat(),
            searchTerms = generateSearchTerms(title, author, genre)
        )

        viewModelScope.launch {

        }
    }

    private fun generateSearchTerms(title: String, author: String, genre: String): List<String> {
        val key = "$title $author $genre".lowercase()
        return key.split(" ")
    }

}

data class UploadBookUiState(
    var error: String? = ""
)