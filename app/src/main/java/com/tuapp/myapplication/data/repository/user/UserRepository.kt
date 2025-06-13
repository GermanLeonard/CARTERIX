package com.tuapp.myapplication.data.repository.user

import com.tuapp.myapplication.data.models.authModels.request.LoginRequestDomain
import com.tuapp.myapplication.data.models.authModels.response.LoginResponseDomain
import com.tuapp.myapplication.data.models.authModels.request.RegisterRequestDomain
import com.tuapp.myapplication.data.models.CommonResponseDomain
import com.tuapp.myapplication.data.models.authModels.UserDataDomain
import com.tuapp.myapplication.data.models.authModels.request.ChangePasswordRequestDomain
import com.tuapp.myapplication.data.models.authModels.request.ChangeProfileRequestDomain
import com.tuapp.myapplication.data.models.authModels.response.ChangeProfileResponseDomain
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getCredentials(): Flow<UserDataDomain>
    suspend fun closeSession()

    suspend fun registerUser(registerRequest: RegisterRequestDomain): Flow<Resource<CommonResponseDomain>>
    suspend fun loginUser(loginRequest: LoginRequestDomain): Flow<Resource<LoginResponseDomain>>
    suspend fun changeProfile(changeProfileRequest: ChangeProfileRequestDomain): Flow<Resource<ChangeProfileResponseDomain>>
    suspend fun changePassword(changePasswordRequest: ChangePasswordRequestDomain): Flow<Resource<CommonResponseDomain>>
}