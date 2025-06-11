package com.tuapp.myapplication.data.repository.sensitive

import kotlinx.coroutines.flow.Flow

interface SensitiveInfoRepository {

    val authenticationToken: Flow<String?>

    suspend fun saveToken(token: String)
    suspend fun clearToken()
}