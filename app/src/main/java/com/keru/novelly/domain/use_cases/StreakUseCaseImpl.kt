package com.keru.novelly.domain.use_cases

import android.util.Log
import com.keru.novelly.data.data_source.local.PreferencesDataSource
import com.keru.novelly.utils.CURRENT_STREAK
import com.keru.novelly.utils.HIGHEST_STREAK
import com.keru.novelly.utils.LAST_OPENED_TIMESTAMP
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class StreakUseCaseImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource
) : StreakUseCase {

    override suspend fun incrementStreak() {
        Log.d("streak", "Incrementing streak")

        val currentStreak = preferencesDataSource.readInt(CURRENT_STREAK).first() + 1
        val highestStreak =
            maxOf(currentStreak, preferencesDataSource.readInt(HIGHEST_STREAK).first())

        Log.d("streak", "Current streak = $currentStreak \nHighest streak = $highestStreak")


        preferencesDataSource.writeInt(CURRENT_STREAK, currentStreak)
        preferencesDataSource.writeInt(HIGHEST_STREAK, highestStreak)
        preferencesDataSource.writeValue(
            LAST_OPENED_TIMESTAMP,
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(System.currentTimeMillis())
        )
    }

    override suspend fun resetStreak() {
        Log.d("streak", "Reseting streak")
        val currentStreak = preferencesDataSource.readInt(CURRENT_STREAK).first() + 1
        val highestStreak =
            maxOf(currentStreak, preferencesDataSource.readInt(HIGHEST_STREAK).first())

        preferencesDataSource.writeInt(CURRENT_STREAK, 0)
        preferencesDataSource.writeInt(HIGHEST_STREAK, highestStreak)
        preferencesDataSource.writeValue(
            LAST_OPENED_TIMESTAMP,
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                System.currentTimeMillis()
            )
        )
    }

}