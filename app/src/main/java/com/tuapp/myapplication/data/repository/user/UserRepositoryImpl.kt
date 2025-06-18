package com.tuapp.myapplication.data.repository.user

import android.util.Log
import com.google.gson.Gson
import com.tuapp.myapplication.data.database.dao.user.UserDao
import com.tuapp.myapplication.data.database.entities.user.toDomain
import com.tuapp.myapplication.data.models.authModels.request.LoginRequestDomain
import com.tuapp.myapplication.data.models.authModels.response.LoginResponseDomain
import com.tuapp.myapplication.data.models.authModels.request.RegisterRequestDomain
import com.tuapp.myapplication.data.models.CommonResponseDomain
import com.tuapp.myapplication.data.models.authModels.UserDataDomain
import com.tuapp.myapplication.data.models.authModels.request.ChangePasswordRequestDomain
import com.tuapp.myapplication.data.models.authModels.request.ChangeProfileRequestDomain
import com.tuapp.myapplication.data.models.authModels.response.ChangeProfileResponseDomain
import com.tuapp.myapplication.data.models.authModels.request.toRequest
import com.tuapp.myapplication.data.remote.responses.authResponse.toDomain
import com.tuapp.myapplication.data.remote.responses.CommonResponse
import com.tuapp.myapplication.data.remote.responses.toDomain
import com.tuapp.myapplication.data.remote.responses.authResponse.toEntity
import com.tuapp.myapplication.data.remote.user.UserService
import com.tuapp.myapplication.data.repository.sensitive.SensitiveInfoRepository
import com.tuapp.myapplication.helpers.Resource
import com.tuapp.myapplication.helpers.errorParsing
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

    override suspend fun checkUserAndDeleteToken() {
        val user = userDao.getUserOnce()
        if(user == null) {
            sensitiveInfoRepository.clearToken()
        }
    }

    override suspend fun closeSession() {
        sensitiveInfoRepository.clearToken()
    }

    override suspend fun registerUser(registerRequest: RegisterRequestDomain): Flow<Resource<CommonResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val registerResponse = userService.registerUser(registerRequest.toRequest())

            if(registerResponse.success) {
                emit(Resource.Success(registerResponse.toDomain()))
            }
        }catch (e: HttpException) {
            //OJO
            //EN EL FRONT
            //MANEJEN LOS ERRORES, CODIGO 400: BAD REQUEST,
            // 409: CONFLICTO (ej. correo ya usado), 500 ERROR DEL SERVIDOR
            val msg = errorParsing(e)

            emit(Resource.Error(httpCode = e.code(), message = msg))
        }catch(e: Exception) {
            Log.d("UserRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun loginUser(loginRequest: LoginRequestDomain): Flow<Resource<LoginResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val loginResponse = userService.loginUser(loginRequest.toRequest())

            sensitiveInfoRepository.saveToken(loginResponse.token)
            userDao.insertUser(loginResponse.datos_user.toEntity())

            emit(Resource.Success(loginResponse.toDomain()))
        } catch (e: HttpException) {
            //OJO
            //EN EL FRONT
            //MANEJEN LOS ERRORES, CODIGO 400: BAD REQUEST, 404: NOT FOUND,
            // 409: CONFLICT (ej. contraseña incorrecta), 500 ERROR DEL SERVIDOR
            val msg = errorParsing(e)

            emit(Resource.Error(httpCode = e.code(), message = msg))
        } catch (e: Exception) {
            Log.d("UserRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun changeProfile(changeProfileRequest: ChangeProfileRequestDomain): Flow<Resource<ChangeProfileResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val changeProfileResponse = userService.changeProfile(changeProfileRequest.toRequest())

            userDao.insertUser(changeProfileResponse.datos_user.toEntity())

            emit(Resource.Success(changeProfileResponse.toDomain()))
        } catch (e: HttpException) {
            //OJO
            //EN EL FRONT
            //MANEJEN LOS ERRORES, CODIGO 400: BAD REQUEST, 404: NOT FOUND,
            //500 ERROR DEL SERVIDOR
            val msg = errorParsing(e)

            emit(Resource.Error(httpCode = e.code(), message = msg))
        } catch (e: Exception){
            Log.d("UserRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequestDomain): Flow<Resource<CommonResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val changePasswordResponse = userService.changePassword(changePasswordRequest.toRequest())

            if(changePasswordResponse.success){
                emit(Resource.Success(changePasswordResponse.toDomain()))
            }

        } catch (e: HttpException) {
            //OJO
            //EN EL FRONT
            //MANEJEN LOS ERRORES, CODIGO 400: BAD REQUEST, 404: NOT FOUND,
            // 409: CONFLICT(ej. contraseñas no coinciden), 500 ERROR DEL SERVIDOR
            val msg = errorParsing(e)

            emit(Resource.Error(httpCode = e.code(), message = msg))
        } catch (e: Exception){
            Log.d("UserRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)
}