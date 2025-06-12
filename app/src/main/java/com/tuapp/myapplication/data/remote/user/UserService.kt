package com.tuapp.myapplication.data.remote.user

import com.tuapp.myapplication.data.remote.request.authRequest.LoginRequest
import com.tuapp.myapplication.data.remote.request.authRequest.RegisterRequest
import com.tuapp.myapplication.data.remote.request.profileRequest.ChangePasswordRequest
import com.tuapp.myapplication.data.remote.request.profileRequest.ChangeProfileRequest
import com.tuapp.myapplication.data.remote.request.profileRequest.ChangeProfileResponse
import com.tuapp.myapplication.data.remote.responses.authResponse.LoginResponse
import com.tuapp.myapplication.data.remote.responses.authResponse.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserService {
    @POST("usuario/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse

    @POST("usuario/registro")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): RegisterResponse

    @PATCH("usuario/cambiar-perfil")
    suspend fun changeProfile(@Body changeProfileRequest: ChangeProfileRequest): ChangeProfileResponse

    @POST("usuario/cambiar-contrasena")
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): RegisterResponse
}