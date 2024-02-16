package com.keru.novelly.ui.pages.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keru.novelly.data.data_source.local.models.Book
import com.keru.novelly.domain.repositories.BooksRepository
import com.keru.novelly.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val booksRepository: BooksRepository
) : ViewModel() {

    var uiState by mutableStateOf(SearchUiState())
        private set

    init {
        getRandomBooks()
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            booksRepository
                .searchBooks(query)
                .collect { resource ->
                    uiState = when (resource) {

                        is Resource.Error -> {
                            uiState.copy(
                                error = resource.message
                            )
                        }

                        is Resource.Success -> {
                            if (resource.data?.isEmpty() == true) getRandomBooks()
                            uiState.copy(
                                books = resource.data,
                                error = null
                            )
                        }

                    }
                }
        }
    }

    private fun getRandomBooks() {
        viewModelScope.launch {
            booksRepository.getRandomBooks().collect { resource ->
                uiState = when (resource) {
                    is Resource.Success -> {
                        uiState.copy(
                            explorationBooks = resource.data,
                            error = null
                        )
                    }

                    is Resource.Error -> {
                        uiState.copy(
                            error = resource.message
                        )
                    }
                }
            }
        }
    }

    fun resetSearchResults() {
        uiState = uiState.copy(
            books = null
        )
    }

}

data class SearchUiState(
    val error: String? = null,
    val books: List<Book>? = emptyList(),
    val explorationBooks: List<Book>? = null
)