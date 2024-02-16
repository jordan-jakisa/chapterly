package com.keru.novelly.ui.core

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keru.novelly.data.data_source.local.PreferencesDataSource
import com.keru.novelly.utils.DARK_MODE_PREFS
import com.keru.novelly.utils.DYNAMIC_THEME_PREFS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val dataStoreDataSource: PreferencesDataSource
) : ViewModel() {

    var uiState by mutableStateOf(AppUiState())
        private set

    init {
        readPreferences()
    }

    private fun readPreferences() {
        viewModelScope.launch {

            dataStoreDataSource.readBoolean(DYNAMIC_THEME_PREFS).collect {

                uiState = uiState.copy(
                    isDynamicColor = it
                )
                Log.d(
                    "Settings",
                    "Reading \n Dynamic Color: ${it}"
                )
            }
        }

        viewModelScope.launch {
            dataStoreDataSource.readBoolean(DARK_MODE_PREFS).collect {


                uiState = uiState.copy(
                    isDarkMode = it
                )
                Log.d(
                    "Settings",
                    "Reading \n DarkMode: ${it}"
                )
            }
        }
    }

}

data class AppUiState(
    val isDarkMode: Boolean = false,
    val isDynamicColor: Boolean = false
)