package com.keru.novelly.ui.pages.settings_page

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
class SettingsViewModel @Inject constructor(
    private val dataStoreDataSource: PreferencesDataSource
) : ViewModel() {

    var uiState by mutableStateOf(SettingsUiState())
        private set

    init {
        refreshPreferences()
    }

    private fun refreshPreferences() {
        viewModelScope.launch {
            dataStoreDataSource.readBoolean(DYNAMIC_THEME_PREFS).collect {
                Log.d(
                    "Settings",
                    "Reading \n Dynamic Color: ${uiState.isDynamicColor}"
                )
                uiState = uiState.copy(
                    isDynamicColor = it
                )
            }
        }

        viewModelScope.launch {
            dataStoreDataSource.readBoolean(DARK_MODE_PREFS).collect {
                Log.d(
                    "Settings",
                    "Reading \n DarkMode: ${uiState.isDarkMode}"
                )

                uiState = uiState.copy(
                    isDarkMode = it
                )
            }
        }
    }

    fun switchValue(name: String, value: Boolean) {
        viewModelScope.launch {
            Log.d("Settings", "Writing: $name \n Value: $value")
            dataStoreDataSource.writeBoolean(name, value)
        }
    }
}

data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val isDynamicColor: Boolean = false
)