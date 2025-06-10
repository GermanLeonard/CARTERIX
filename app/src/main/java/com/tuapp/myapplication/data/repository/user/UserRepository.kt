package com.tuapp.myapplication.data.repository.user

import com.tuapp.myapplication.data.models.authModels.LoginRequestDomain
import com.tuapp.myapplication.data.models.authModels.LoginResponseDomain
import com.tuapp.myapplication.helpers.Resourse
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun loginUser(loginRequest: LoginRequestDomain): Flow<Resourse<LoginResponseDomain>>
}