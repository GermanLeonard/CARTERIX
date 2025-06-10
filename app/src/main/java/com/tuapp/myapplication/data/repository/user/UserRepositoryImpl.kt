package com.tuapp.myapplication.data.repository.user

import com.google.gson.Gson
import com.tuapp.myapplication.data.models.authModels.LoginRequestDomain
import com.tuapp.myapplication.data.models.authModels.LoginResponseDomain
import com.tuapp.myapplication.data.models.authModels.toRequest
import com.tuapp.myapplication.data.remote.responses.authResponse.LoginResponse
import com.tuapp.myapplication.data.remote.user.UserService
import com.tuapp.myapplication.helpers.Resourse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class UserRepositoryImpl(
    private val userService: UserService
): UserRepository {

    override suspend fun loginUser(loginRequestDomain: LoginRequestDomain): Flow<Resourse<LoginResponseDomain>> = flow {
        emit(Resourse.Loading)

        try {
            val loginResponse = userService.loginUser(loginRequestDomain.toRequest())

        } catch (e: HttpException) {
            if(e.code() == 409) {
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()

                val errorResponse = gson.fromJson(errorBody, LoginResponse::class.java)
                val msg = errorResponse.message ?: "Error de autentificación"

                Resourse.Error(msg)
            }
        }
    }
}