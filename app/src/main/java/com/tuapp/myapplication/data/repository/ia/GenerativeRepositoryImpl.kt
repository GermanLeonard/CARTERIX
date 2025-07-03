package com.tuapp.myapplication.data.repository.ia

import com.google.firebase.ai.GenerativeModel
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GenerativeRepositoryImpl(
    private val generativeModel: GenerativeModel
): GenerativeRepository{

    override suspend fun generateAdvice(prompt: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading)
        try {
            val response = generativeModel.generateContent(prompt)

            val text = response.text
            if(text.isNullOrEmpty()){
                emit(Resource.Error(message = "No hay contenido"))
            } else {
                emit(Resource.Success(text))
            }
        } catch(e: Exception) {
            emit(Resource.Error(message = "Error al generar el contenido: ${e.message}"))
        }
    }
}