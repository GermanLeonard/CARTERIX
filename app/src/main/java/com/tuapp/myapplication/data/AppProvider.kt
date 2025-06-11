package com.tuapp.myapplication.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.tuapp.myapplication.data.repository.sensitive.SensitiveInfoRepository
import com.tuapp.myapplication.data.repository.sensitive.SensitiveInfoRepositoryImpl

private const val SENSITIVE_INFO_NAME = "sensitive_info"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SENSITIVE_INFO_NAME)

class AppProvider(context: Context) {

    private val sensitiveInfoRepository: SensitiveInfoRepository = SensitiveInfoRepositoryImpl(context.dataStore)

    fun provideSensitiveInfoRepository(): SensitiveInfoRepository {
        return sensitiveInfoRepository
    }
}