package com.keru.novelly.ui.pages.downloads

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keru.novelly.data.data_source.local.DownloadedBooksLocalDataSource
import com.keru.novelly.data.data_source.local.PreferencesDataSource
import com.keru.novelly.data.data_source.local.models.DownloadedFile
import com.keru.novelly.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val downloadedBooksLocalDataSource: DownloadedBooksLocalDataSource,
    private val prefsDataSource: PreferencesDataSource
) : ViewModel() {

    var uiState by mutableStateOf(DownloadsPageUiState())
        private set

    init {
        getDownloadedBooks()
    }

    fun getDownloadedBooks() {
        viewModelScope.launch {
            downloadedBooksLocalDataSource.invoke().flowOn(Dispatchers.IO).collect { resource ->
                uiState = when (resource) {
                    is Resource.Success -> {
                        val sortedBooksList = resource.data?.sortedByDescending {
                            it.lastModified()
                        }
                        val downloadedFilesList = mutableListOf<DownloadedFile>()

                        sortedBooksList?.take(2)?.let {
                            it.forEach { file ->
                                val readingProgress = getBookReadingProgress(file.name)
                                Log.d(
                                    "Pref",
                                    "Book ==> ${file.name} \n Progress ==> $readingProgress"
                                )
                                val downloadedFile = DownloadedFile(
                                    file,
                                    readingProgress
                                )
                                downloadedFilesList.add(downloadedFile)
                            }
                        }

                        uiState.copy(
                            books = sortedBooksList?.drop(2) ?: emptyList(),
                            error = null,
                            booksInProgress = downloadedFilesList.sortedByDescending {
                                it.file.lastModified()
                            }
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

    private suspend fun getBookReadingProgress(name: String): Float {
        val progress = prefsDataSource.readValue(name).first()
        Log.d("Prefs", "Progress for : $name \n Value: $progress ")
        return progress.toFloat()
    }

    fun refreshList() {
        getDownloadedBooks()
    }
}

data class DownloadsPageUiState(
    val books: List<File> = emptyList(),
    val error: String? = null,
    val booksInProgress: List<DownloadedFile> = emptyList()
)