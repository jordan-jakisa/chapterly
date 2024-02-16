package com.keru.novelly.ui.pages.pdf_reader

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keru.novelly.data.data_source.local.PreferencesDataSource
import com.keru.novelly.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
    private val userRepository: UserRepository
) : ViewModel() {

    fun updateBookProgress(name: String, value: String) {
        viewModelScope.launch {
            preferencesDataSource.writeValue(name, value)
            Log.d("Prefs", "Writing: $name \n Value: $value ")

        }
    }

    suspend fun getPrefValue(name: String): String {
        val value = preferencesDataSource.readValue(name).first()
        Log.d("Prefs", "Getting: $name \n Value: $value ")
        return value
    }

    fun addBookToCompleted(name: String) {
        viewModelScope.launch {
            userRepository.addBookToCompleted(name)
        }
    }

}