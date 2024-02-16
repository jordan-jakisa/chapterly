package com.keru.novelly.domain.use_cases

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.keru.novelly.data.data_source.local.models.Book
import com.keru.novelly.domain.repositories.BooksRepository
import com.keru.novelly.domain.repositories.UserRepository
import javax.inject.Inject

class DownloadBookUseCase @Inject constructor(
    private val context: Context,
    private val downloadManager: DownloadManager,
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val bookRepository: BooksRepository
) {
    suspend operator fun invoke(book: Book) {
        val request = DownloadManager.Request(book.book.toUri()).apply {
            setAllowedOverMetered(true)
            setTitle(book.title)
            setDescription("Downloading from ${book.book}")
            setDestinationUri(Uri.parse("file://"))
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_DOWNLOADS,
                book.title
            )
        }

        try {
            downloadManager.enqueue(request)
            bookRepository.incrementDownloadCount(book.bid)
            val currentUser = auth.currentUser
            if (currentUser != null) {
                userRepository.updateGenre(book.category)
            }
        } catch (e: Exception) {
            Log.d("BooksTest", "Exception ==> ${e.message}")
        }
    }
}