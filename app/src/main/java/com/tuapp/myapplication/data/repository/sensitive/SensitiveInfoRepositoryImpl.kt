package com.tuapp.myapplication.data.repository.sensitive

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException

class SensitiveInfoRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): SensitiveInfoRepository {
    private companion object {
        val AUTHENTICATION_TOKEN = stringPreferencesKey("AUTHENTICATION_TOKEN")
    }

    override val authenticationToken: Flow<String?> = dataStore.data
        .catch {
            if(it is IOException) {
                emit(emptyPreferences())
            }else {
                throw it
            }
        }
        .map { preferences ->
            preferences[AUTHENTICATION_TOKEN] ?: null
        }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTHENTICATION_TOKEN] = token
        }
    }

    override suspend fun clearToken() {
        dataStore.edit { preferences ->
           preferences.remove(AUTHENTICATION_TOKEN)
        }
    }
}