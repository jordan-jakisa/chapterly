package com.keru.novelly.ui.pages.completed_books

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keru.novelly.data.data_source.local.models.Book
import com.keru.novelly.data.data_source.network.models.User
import com.keru.novelly.domain.repositories.BooksRepository
import com.keru.novelly.domain.repositories.UserRepository
import com.keru.novelly.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompletedBooksViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private var user: User? = null
    var uiState by mutableStateOf(CompletedBooksUiState())
        private set

    init {
        getUserDetails()
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            userRepository.getUserDetails().collect {
                when (it) {
                    is Resource.Success -> {
                        Log.d("booksread", "Gotten user data!")
                        user = it.data
                        getCompletedBooks(user?.completedBooks!!)
                        Log.d("booksread", "Genres: ${it.data?.genresDownloaded}!")
                    }

                    is Resource.Error -> {
                        Log.d("booksread", "Error: ${it.message}")
                    }
                }
            }

        }
    }

    private fun getCompletedBooks(names: List<String>) {
        Log.d("booksread", "Completed books : $names")
        val compBooks = names.map { it.removeSuffix(".pdf") }
        Log.d("booksread", "Completed books : $compBooks")


        viewModelScope.launch {
            booksRepository.getCompletedBooks(compBooks).collect { resource ->
                uiState = when (resource) {
                    is Resource.Success -> {
                        Log.d("booksread", "Books gotten")
                        uiState.copy(
                            books = resource.data!!,
                            isLoading = false
                        )
                    }

                    is Resource.Error -> {
                        Log.d("booksread", "Error: ${resource.message}")
                        uiState.copy(
                            error = resource.message,
                            isLoading = false
                        )
                    }
                }

            }
        }
    }


}

data class CompletedBooksUiState(
    val books: List<Book> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = true
)