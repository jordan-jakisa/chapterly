package com.keru.novelly.ui.pages.book_details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.keru.novelly.data.data_source.local.models.Book
import com.keru.novelly.domain.repositories.BooksRepository
import com.keru.novelly.domain.use_cases.DownloadBookUseCase
import com.keru.novelly.domain.use_cases.UsersInteractionsUseCase
import com.keru.novelly.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val booksRepository: BooksRepository,
    private val downloadBookUseCase: DownloadBookUseCase,
    private val userInteractionsUseCase: UsersInteractionsUseCase,
    private val fireStore: FirebaseFirestore
) : ViewModel() {

    var uiState by mutableStateOf(BookDetailsUiState())
        private set

    init {
        getSignInStatus()
    }

    fun getBook(bookId: String) {
        viewModelScope.launch {
            Log.d("downloads", "getBook()")
            /*val hasLiked =
                if (bookId.isNotEmpty()) userInteractionsUseCase.hasUserLikedBook(bookId) else false

            fireStore.collection(FirebasePaths.Books.path).document(bookId)
                .addSnapshotListener { snapshot, error ->
                    Log.d("downloads", "Listening ...")

                    if (snapshot!!.exists()) {
                        val book = snapshot.toObject(Book::class.java)
                        uiState = uiState.copy(
                            book = book,
                            isLiked = hasLiked
                        )
                        Log.d("downloads", "Book : ${book?.title}")
                    } else {
                        Log.d("downloads", "Snapshot does not exist")
                        uiState = uiState.copy(
                            error = "An unknown error occurred!"
                        )
                    }

                }*/

            booksRepository.getBookDetails(bookId).collect {
                uiState = when (it) {
                    is Resource.Success -> {
                        uiState.copy(
                            book = it.data,
                            isLiked = userInteractionsUseCase.hasUserLikedBook(it.data!!.bid)
                        )
                    }

                    is Resource.Error -> {
                        uiState.copy(
                            error = it.message
                        )
                    }
                }
            }

        }
    }

    fun downloadBook(book: Book) {
        viewModelScope.launch {
            downloadBookUseCase.invoke(book)
        }
    }

    private fun getSignInStatus() {
        uiState = uiState.copy(
            isUserSignedIn = auth.currentUser != null
        )
    }

    fun likeBook(bookId: String) {
        viewModelScope.launch {
            when (val liked = userInteractionsUseCase.likeBook(bookId)) {
                is Resource.Success -> {
                    if (liked.data == true) {
                        uiState = uiState.copy(
                            isLiked = userInteractionsUseCase.hasUserLikedBook(bookId)
                        )
                    }
                }

                is Resource.Error -> {
                    uiState = uiState.copy(
                        error = liked.message
                    )
                }
            }
        }
    }

    fun disLikeBook(bookId: String) {
        viewModelScope.launch {
            when (val liked = userInteractionsUseCase.disLikeBook(bookId)) {
                is Resource.Success -> {
                    if (liked.data == true) {
                        uiState = uiState.copy(
                            isLiked = userInteractionsUseCase.hasUserLikedBook(bookId)
                        )
                    }
                }

                is Resource.Error -> {
                    uiState = uiState.copy(
                        error = liked.message
                    )
                }
            }
        }
    }

}

data class BookDetailsUiState(
    val book: Book? = null,
    val error: String? = null,
    val isUserSignedIn: Boolean = false,
    val isLiked: Boolean = false
)