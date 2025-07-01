package com.tuapp.myapplication.data.repository.ia

import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.Flow

interface GenerativeRepository {
    suspend fun generateAdvice(prompt: String): Flow<Resource<String>>
}