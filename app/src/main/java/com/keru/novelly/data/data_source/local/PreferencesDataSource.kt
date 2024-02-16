package com.keru.novelly.data.data_source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    suspend fun writeValue(name: String, value: String) {
        dataStore.edit {
            it[stringPreferencesKey(name)] = value
        }
    }

    suspend fun writeBoolean(name: String, value: Boolean) {
        dataStore.edit {
            it[booleanPreferencesKey(name)] = value
        }
    }

    suspend fun readBoolean(name: String): Flow<Boolean> {
        return dataStore.data.map {
            it[booleanPreferencesKey(name)] ?: false
        }
    }

    suspend fun readValue(name: String): Flow<String> {
        return dataStore.data.map {
            it[stringPreferencesKey(name)] ?: "0"
        }
    }

    suspend fun writeLong(name: String, value: Long) {
        dataStore.edit {
            it[longPreferencesKey(name)] = value
        }
    }

    suspend fun readLong(name: String): Flow<Long> {
        return dataStore.data.map {
            it[longPreferencesKey(name)] ?: 0L
        }
    }

    suspend fun writeInt(name: String, value: Int) {
        dataStore.edit {
            it[intPreferencesKey(name)] = value
        }
    }

    suspend fun readInt(name: String): Flow<Int> {
        return dataStore.data.map {
            it[intPreferencesKey(name)] ?: 0
        }
    }

}