package com.keru.novelly.data.schedulers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.keru.novelly.data.data_source.local.PreferencesDataSource
import com.keru.novelly.domain.use_cases.StreakUseCase
import com.keru.novelly.utils.LAST_OPENED_TIMESTAMP
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@HiltWorker
class StreakWorker @AssistedInject constructor(
    @Assisted private val preferenceDataSource: PreferencesDataSource,
    @Assisted private val streakUseCase: StreakUseCase,
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("streak", "Doing work")
        preferenceDataSource.writeValue(
            LAST_OPENED_TIMESTAMP,
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(System.currentTimeMillis())
        )

        return try {
            val currentDate = getCurrentDate()
            val lastOpenedDate = getLastOpenedDate()

            if (isConsecutiveDay(lastOpenedDate, currentDate)) {
                streakUseCase.incrementStreak()
            } else {
                streakUseCase.resetStreak()
            }
            Log.d("streak", "Success")
            Result.success()
        } catch (e: Exception) {
            Log.d("streak", "Error: ${e.message}")
            Result.failure()
        }
    }

    private fun getCurrentDate(): Date {
        return Calendar.getInstance().time
    }

    private suspend fun getLastOpenedDate(): Date? {
        val dateString = preferenceDataSource.readValue(LAST_OPENED_TIMESTAMP).first()
        return dateString.let {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)
        }
    }

    private fun isConsecutiveDay(lastDate: Date?, currentDate: Date): Boolean {
        return lastDate != null && isSameDay(lastDate, currentDate)
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
    }
}