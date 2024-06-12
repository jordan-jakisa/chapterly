package com.keru.novelly.ui.pages.more_page

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.keru.novelly.data.data_source.local.models.Book
import com.keru.novelly.data.data_source.network.models.User
import com.keru.novelly.domain.repositories.UserRepository
import com.keru.novelly.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository,
) : ViewModel() {

    var uiState by mutableStateOf(MorePageUiState())
        private set

    init {
        getSignInStatus()

    }

    private fun getSignInStatus() {
        val currentUser = auth.currentUser
        uiState = uiState.copy(
            isUserSignedIn = auth.currentUser != null
        )

        if (currentUser != null) {
            getUser()
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            userRepository.getUserDetails().collect { user ->
                uiState = when (user) {
                    is Resource.Error -> {
                        uiState.copy(
                            error = user.message
                        )
                    }

                    is Resource.Success -> {
                        uiState.copy(
                            user = user.data,
                            favoriteGenre = user.data?.genresDownloaded?.groupingBy { it }
                                ?.eachCount()
                                ?.maxByOrNull { it.value }?.key
                        )

                    }
                }
            }

        }
    }

    fun signOut() {
        auth.signOut()
        uiState = uiState.copy(
            isUserSignedIn = false,
            user = null
        )
    }

    fun deleteUser() {
        viewModelScope.launch {
            when (val task = userRepository.deleteUserDetails()) {
                is Resource.Success -> {
                    uiState = uiState.copy(
                        error = task.data
                    )
                }

                is Resource.Error -> {
                    uiState = uiState.copy(
                        error = task.message
                    )
                }
            }

        }
    }

    fun updateUserDetails(uri: Uri?, name: String) {
        viewModelScope.launch {
            if (uri != null) {
                when (val result = userRepository.uploadUserImage(uri)) {
                    is Resource.Success -> {
                        updateUserNameImage(result.data!!, name)
                    }

                    is Resource.Error -> {
                        uiState = uiState.copy(
                            error = result.message
                        )
                        updateUserNameImage(image = null, name = name)
                    }
                }
            } else {
                updateUserNameImage(null, name)
            }
        }
    }

    private fun updateUserNameImage(image: String?, name: String) {
        viewModelScope.launch {
            when (val resource = userRepository.updateUserDetails(name, image)) {
                is Resource.Error -> {
                    uiState = uiState.copy(
                        error = resource.message
                    )
                }

                is Resource.Success -> {
                    uiState = uiState.copy(
                        message = resource.data
                    )
                }
            }
        }
    }

}

data class MorePageUiState(
    val isUserSignedIn: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val favoriteGenre: String? = null,
    val recommendedBooks: List<Book> = emptyList(),
    val message: String? = null
)