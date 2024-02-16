package com.keru.novelly.ui.pages.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keru.novelly.data.data_source.local.PreferencesDataSource
import com.keru.novelly.data.data_source.local.models.Book
import com.keru.novelly.data.data_source.network.models.User
import com.keru.novelly.domain.repositories.BooksRepository
import com.keru.novelly.domain.repositories.UserRepository
import com.keru.novelly.utils.CURRENT_STREAK
import com.keru.novelly.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    private val userRepository: UserRepository,
    private val preferencesDataSource: PreferencesDataSource
) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        getRandomBooks()
        getUserDetails()
        getPopularBooks()
        getCurrentStreak()
    }

    private fun getRandomBooks() {
        viewModelScope.launch {
            booksRepository.getRandomBooks().collect { resource ->
                uiState = when (resource) {
                    is Resource.Success -> {
                        if (resource.data?.isEmpty() == true || resource.data == null) {
                            getRandomBooks()
                            uiState.copy(
                                explorationBooks = null,
                            )
                        } else {
                            uiState.copy(
                                explorationBooks = resource.data,
                                error = null
                            )
                        }
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

    private fun getPopularBooks() {
        viewModelScope.launch {
            booksRepository.getPopularBooks().collect { resource ->
                uiState = when (resource) {
                    is Resource.Success -> {
                        uiState.copy(
                            popularBooks = resource.data,
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

    private fun getUserDetails() {
        viewModelScope.launch {
            val resource = userRepository.getUserDetails().collect {
                viewModelScope.launch {
                    uiState = when (it) {
                        is Resource.Success -> {
                            uiState.copy(
                                user = it.data,
                                error = null
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


    }

    private fun getCurrentStreak() {
        viewModelScope.launch {
            preferencesDataSource.readInt(CURRENT_STREAK).collect {
                uiState = uiState.copy(
                    currentStreak = it
                )
            }
        }
    }

}

data class HomeUiState(
    val explorationBooks: List<Book>? = null,
    val error: String? = null,
    val user: User? = null,
    val popularBooks: List<Book>? = null,
    val currentStreak: Int = 0
)