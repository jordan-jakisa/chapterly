package com.keru.novelly.ui.core

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.google.firebase.database.FirebaseDatabase
import com.keru.novelly.data.data_source.local.PreferencesDataSource
import com.keru.novelly.data.schedulers.StreakWorker
import com.keru.novelly.domain.use_cases.StreakUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: StreakWorkerFactory

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        FirebaseDatabase.getInstance().setPersistenceCacheSizeBytes(2000000)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory).build()

}

class StreakWorkerFactory @Inject constructor(
    private val preferenceDataSource: PreferencesDataSource,
    private val streakUseCase: StreakUseCase,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = StreakWorker(
        preferenceDataSource,
        streakUseCase,
        context = appContext,
        workerParameters
    )

}