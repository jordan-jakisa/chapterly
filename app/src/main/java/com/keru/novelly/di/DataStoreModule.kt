package com.keru.novelly.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.keru.novelly.data.data_source.local.PreferencesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.json.JSONObject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "NOVELLY_PREFERENCES")

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context) =
        context.dataStore

    @Provides
    @Singleton
    fun providesPreferencesDataSource(dataStore: DataStore<Preferences>) =
        PreferencesDataSource(dataStore)

    @Provides
    @Singleton
    fun providesJsonObject() = JSONObject()


}
