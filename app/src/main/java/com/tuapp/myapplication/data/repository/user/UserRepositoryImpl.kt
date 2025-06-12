package com.tuapp.myapplication.data.repository.user

import android.util.Log
import com.google.gson.Gson
import com.tuapp.myapplication.data.database.dao.user.UserDao
import com.tuapp.myapplication.data.database.entities.user.toDomain
import com.tuapp.myapplication.data.models.authModels.LoginRequestDomain
import com.tuapp.myapplication.data.models.authModels.LoginResponseDomain
import com.tuapp.myapplication.data.models.authModels.RegisterRequestDomain
import com.tuapp.myapplication.data.models.authModels.RegisterResponseDomain
import com.tuapp.myapplication.data.models.authModels.UserDataDomain
import com.tuapp.myapplication.data.models.authModels.editProfile.ChangePasswordRequestDomain
import com.tuapp.myapplication.data.models.authModels.editProfile.ChangeProfileRequestDomain
import com.tuapp.myapplication.data.models.authModels.editProfile.ChangeProfileResponseDomain
import com.tuapp.myapplication.data.models.authModels.editProfile.toRequest
import com.tuapp.myapplication.data.models.authModels.toRequest
import com.tuapp.myapplication.data.remote.request.profileRequest.ChangeProfileResponse
import com.tuapp.myapplication.data.remote.request.profileRequest.toDomain
import com.tuapp.myapplication.data.remote.responses.authResponse.LoginResponse
import com.tuapp.myapplication.data.remote.responses.authResponse.RegisterResponse
import com.tuapp.myapplication.data.remote.responses.authResponse.toDomain
import com.tuapp.myapplication.data.remote.responses.authResponse.toEntity
import com.tuapp.myapplication.data.remote.user.UserService
import com.tuapp.myapplication.data.repository.sensitive.SensitiveInfoRepository
import com.tuapp.myapplication.helpers.Resourse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class UserRepositoryImpl(
    private val userService: UserService,
    private val userDao: UserDao,
    private val sensitiveInfoRepository: SensitiveInfoRepository
): UserRepository {

    override fun getCredentials(): Flow<UserDataDomain> {
        return userDao.getUser().map { it.toDomain() }
    }

    override suspend fun closeSession() {
        sensitiveInfoRepository.clearToken()
    }

    override suspend fun registerUser(registerRequest: RegisterRequestDomain): Flow<Resourse<RegisterResponseDomain>> = flow {
        emit(Resourse.Loading)
        try {
            val registerResponse = userService.registerUser(registerRequest.toRequest())

            if(registerResponse.success) {
                emit(Resourse.Success(registerResponse.toDomain()))
            }
        }catch (e: HttpException) {
            //OJO
            //EN EL FRONT
            //MANEJEN LOS ERRORES, CODIGO 400: BAD REQUEST,
            // 409: CONFLICTO (ej. correo ya usado), 500 ERROR DEL SERVIDOR
            val errorBody = e.response()?.errorBody()?.string()
            val gson = Gson()

            val errorResponse = gson.fromJson(errorBody, RegisterResponse::class.java)
            val msg = errorResponse.message

            emit(Resourse.Error(httpCode = e.code(), message = msg))
        }catch(e: Exception) {
            Log.d("UserRepository", "Error al hacer la petición: ${e.message}")
            emit(Resourse.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun loginUser(loginRequestDomain: LoginRequestDomain): Flow<Resourse<LoginResponseDomain>> = flow {
        emit(Resourse.Loading)
        try {
            val loginResponse = userService.loginUser(loginRequestDomain.toRequest())

            if(loginResponse.success && loginResponse.token != null) {
                sensitiveInfoRepository.saveToken(loginResponse.token)
            }

            if(loginResponse.datos_user != null) {
                userDao.insertUser(loginResponse.datos_user.toEntity())
            }

            emit(Resourse.Success(loginResponse.toDomain()))
        } catch (e: HttpException) {
            //OJO
            //EN EL FRONT
            //MANEJEN LOS ERRORES, CODIGO 400: BAD REQUEST, 404: NOT FOUND,
            // 409: CONFLICT (ej. contraseña incorrecta), 500 ERROR DEL SERVIDOR
                val errorBody = e.response()?.errorBody()?.string()
                val gson = Gson()

                val errorResponse = gson.fromJson(errorBody, LoginResponse::class.java)
                val msg = errorResponse.message ?: "Error Inesperado"

                emit(Resourse.Error(httpCode = e.code(), message = msg))
        } catch (e: Exception) {
            Log.d("UserRepository", "Error al hacer la petición: ${e.message}")
            emit(Resourse.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun changeProfile(changeProfileRequest: ChangeProfileRequestDomain): Flow<Resourse<ChangeProfileResponseDomain>> = flow {
        emit(Resourse.Loading)
        try {
            val changeProfileResponse = userService.changeProfile(changeProfileRequest.toRequest())

            if(changeProfileResponse.datos_user != null) {
                userDao.insertUser(changeProfileResponse.datos_user.toEntity())
            }

            emit(Resourse.Success(changeProfileResponse.toDomain()))
        } catch (e: HttpException) {
            //OJO
            //EN EL FRONT
            //MANEJEN LOS ERRORES, CODIGO 400: BAD REQUEST, 404: NOT FOUND,
            //500 ERROR DEL SERVIDOR
            val errorBody = e.response()?.errorBody()?.string()
            val gson = Gson()

            val errorResponse = gson.fromJson(errorBody, ChangeProfileResponse::class.java)
            val msg = errorResponse.message

            emit(Resourse.Error(httpCode = e.code(), message = msg))
        } catch (e: Exception){
            Log.d("UserRepository", "Error al hacer la petición: ${e.message}")
            emit(Resourse.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequestDomain): Flow<Resourse<RegisterResponseDomain>> = flow {
        emit(Resourse.Loading)
        try {
            val changePasswordResponse = userService.changePassword(changePasswordRequest.toRequest())

            if(changePasswordResponse.success){
                emit(Resourse.Success(changePasswordResponse.toDomain()))
            }

        } catch (e: HttpException) {
            //OJO
            //EN EL FRONT
            //MANEJEN LOS ERRORES, CODIGO 400: BAD REQUEST, 404: NOT FOUND,
            // 409: CONFLICT(ej. contraseñas no coinciden), 500 ERROR DEL SERVIDOR
            val errorBody = e.response()?.errorBody()?.string()
            val gson = Gson()

            val errorResponse = gson.fromJson(errorBody, ChangeProfileResponse::class.java)
            val msg = errorResponse.message

            emit(Resourse.Error(httpCode = e.code(), message = msg))
        } catch (e: Exception){
            Log.d("UserRepository", "Error al hacer la petición: ${e.message}")
            emit(Resourse.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)
}