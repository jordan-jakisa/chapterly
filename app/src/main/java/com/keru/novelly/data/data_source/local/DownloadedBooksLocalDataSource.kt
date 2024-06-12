package com.keru.novelly.data.data_source.local

import android.content.Context
import android.os.Environment
import android.util.Log
import com.keru.novelly.utils.Resource
import com.keru.novelly.utils.UNKNOWN_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class DownloadedBooksLocalDataSource @Inject constructor(
    private val context: Context,
) {

    operator fun invoke(): Flow<Resource<List<File>>> = flow {
        try {
            val files = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), ""
            ).listFiles()?.toList()

            Log.d("Nest", "Downloaded books ==> ${files?.size}")
            if (files != null) emit(Resource.Success(data = files))
            else emit(Resource.Error(message = "Files are null!"))

        } catch (e: Exception) {
            emit(Resource.Error(message = e.message ?: UNKNOWN_ERROR))
        }
    }
}