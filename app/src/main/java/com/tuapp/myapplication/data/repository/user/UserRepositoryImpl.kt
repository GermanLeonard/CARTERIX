package com.tuapp.myapplication.data.repository.user

import android.util.Log
import com.google.gson.Gson
import com.tuapp.myapplication.data.models.authModels.LoginRequestDomain
import com.tuapp.myapplication.data.models.authModels.LoginResponseDomain
import com.tuapp.myapplication.data.models.authModels.toRequest
import com.tuapp.myapplication.data.remote.responses.authResponse.LoginResponse
import com.tuapp.myapplication.data.remote.responses.authResponse.toDomain
import com.tuapp.myapplication.data.remote.user.UserService
import com.tuapp.myapplication.data.repository.sensitive.SensitiveInfoRepository
import com.tuapp.myapplication.helpers.Resourse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class UserRepositoryImpl(
    private val userService: UserService,
    private val sensitiveInfoRepository: SensitiveInfoRepository
): UserRepository {

    override suspend fun loginUser(loginRequestDomain: LoginRequestDomain): Flow<Resourse<LoginResponseDomain>> = flow {
        emit(Resourse.Loading)
        try {
            val loginResponse = userService.loginUser(loginRequestDomain.toRequest())

            if(loginResponse.success && loginResponse.token != null) {
                sensitiveInfoRepository.saveToken(loginResponse.token)
            }

            emit(Resourse.Success(loginResponse.toDomain()))
        } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()

                val errorResponse = gson.fromJson(errorBody, LoginResponse::class.java)
                val msg = errorResponse.message ?: "Error de autentificación"

                emit(Resourse.Error("${e.code()} $msg"))
        } catch (e: Exception) {
            Log.d("UserRepository", "Error fetching movies: ${e.message}")
            emit(Resourse.Error("Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)
}