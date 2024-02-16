package com.keru.novelly.ui.pages.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore
) : ViewModel() {
    private var _uiState = MutableStateFlow(SignupUI())
    val uiState: StateFlow<SignupUI> = _uiState
    fun registerUser(email: String, password: String) = viewModelScope.launch {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                authResult.user?.let {
                    db.collection("Users").document(it.uid).set(
                        ""
                        //User(uid = it.uid, email = email, joinedAt = System.currentTimeMillis())
                    ).addOnSuccessListener {
                        _uiState.value = _uiState.value.copy(
                            success = true
                        )
                    }.addOnFailureListener { e ->
                        _uiState.value = _uiState.value.copy(
                            response = e.localizedMessage!!.toString()
                        )
                    }
                }
            }
            .addOnFailureListener {
                _uiState.value = _uiState.value.copy(
                    response = it.localizedMessage!!.toString()
                )
            }

    }
}

data class SignupUI(
    val response: String = "",
    val success: Boolean = false
)