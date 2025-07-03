package com.tuapp.myapplication.data.repository.savings

import android.util.Log
import com.tuapp.myapplication.data.database.dao.saving.SavingsDao
import com.tuapp.myapplication.data.database.dao.user.UserDao
import com.tuapp.myapplication.data.database.entities.savings.toDomain
import com.tuapp.myapplication.data.models.CommonResponseDomain
import com.tuapp.myapplication.data.models.savingsModels.request.CreateOrUpdateSavingDomain
import com.tuapp.myapplication.data.models.savingsModels.request.toRequest
import com.tuapp.myapplication.data.models.savingsModels.response.SavingsDataDomain
import com.tuapp.myapplication.data.remote.responses.savingResponse.toEntity
import com.tuapp.myapplication.data.remote.responses.toDomain
import com.tuapp.myapplication.data.remote.saving.SavingService
import com.tuapp.myapplication.helpers.Resource
import com.tuapp.myapplication.helpers.errorParsing
import com.tuapp.myapplication.helpers.getFinanceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class SavingsRepositoryImpl(
    private val savingsService: SavingService,
    private val savingsDao: SavingsDao,
    private val userDao: UserDao,
): SavingsRepository {

    override suspend fun getSavingsData(finanzaId: Int?, anio: Int): Flow<Resource<List<SavingsDataDomain>>> = flow {
        emit(Resource.Loading)
        try {
            val savingsResponse = savingsService.getSavingsData(anio, finanzaId)

            if(savingsResponse.data.isNotEmpty()){
                savingsDao.clearSavingData(
                    finanzaId ?: getFinanceId(userDao)
                )
                savingsDao.insertSavingData(savingsResponse.toEntity())
            }else {
                emit(Resource.Success(emptyList()))
                return@flow
            }
        }catch (e :HttpException) {
            //ERRORES
            //400: BAD REQUEST, 500: ERROR DEL SERVER
            val msg = errorParsing(e)

            emit(Resource.Error(httpCode = e.code(), message = msg))
            return@flow
        }catch (e: Exception) {
            Log.d("SavingsRepositoryImpl", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
            return@flow
        }

        val savingsList = savingsDao.getSavingsData(
            finanzaId ?: getFinanceId(userDao), anio
        ).map { entities ->
            val list = entities.map { it.toDomain() }

            Resource.Success(list)
        }.distinctUntilChanged()
        emitAll(savingsList)
    }.flowOn(Dispatchers.IO)

    override suspend fun createOrUpdateSavings(
        finanzaId: Int?,
        createOrUpdateSaving: CreateOrUpdateSavingDomain
    ): Flow<Resource<CommonResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val createResponse = savingsService.createOrUpdateSavingGoal(finanzaId, createOrUpdateSaving.toRequest())

            emit(Resource.Success(createResponse.toDomain()))
        }catch (e :HttpException) {
            //ERRORES
            //400: BAD REQUEST, 500: ERROR DEL SERVER
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        }catch (e: Exception) {
            Log.d("SavingsRepositoryImpl", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)
}