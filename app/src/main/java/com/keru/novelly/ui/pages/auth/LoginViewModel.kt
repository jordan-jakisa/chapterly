package com.keru.novelly.ui.pages.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val auth: FirebaseAuth,
) : ViewModel() {
    private var _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun signInUser(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(
                email, password
            ).addOnSuccessListener {
                _uiState.value = _uiState.value.copy(
                    isSuccess = true
                )
            }.addOnFailureListener {
                _uiState.value = _uiState.value.copy(
                    response = "Error: ${it.localizedMessage}"
                )
            }
        }
        }


}

data class LoginUiState(
    val isSuccess: Boolean = false,
    val response: String = ""
)