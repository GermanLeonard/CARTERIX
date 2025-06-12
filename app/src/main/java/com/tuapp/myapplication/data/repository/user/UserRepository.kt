package com.tuapp.myapplication.data.repository.user

import com.tuapp.myapplication.data.models.authModels.LoginRequestDomain
import com.tuapp.myapplication.data.models.authModels.LoginResponseDomain
import com.tuapp.myapplication.data.models.authModels.RegisterRequestDomain
import com.tuapp.myapplication.data.models.authModels.RegisterResponseDomain
import com.tuapp.myapplication.data.models.authModels.UserDataDomain
import com.tuapp.myapplication.data.models.authModels.editProfile.ChangePasswordRequestDomain
import com.tuapp.myapplication.data.models.authModels.editProfile.ChangeProfileRequestDomain
import com.tuapp.myapplication.data.models.authModels.editProfile.ChangeProfileResponseDomain
import com.tuapp.myapplication.helpers.Resourse
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getCredentials(): Flow<UserDataDomain>

    suspend fun registerUser(registerRequest: RegisterRequestDomain): Flow<Resourse<RegisterResponseDomain>>
    suspend fun loginUser(loginRequest: LoginRequestDomain): Flow<Resourse<LoginResponseDomain>>
    suspend fun changeProfile(changeProfileRequest: ChangeProfileRequestDomain): Flow<Resourse<ChangeProfileResponseDomain>>
    suspend fun changePassword(changePasswordRequest: ChangePasswordRequestDomain): Flow<Resourse<RegisterResponseDomain>>
}