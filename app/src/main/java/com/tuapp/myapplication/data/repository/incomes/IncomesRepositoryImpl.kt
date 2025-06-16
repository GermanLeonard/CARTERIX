package com.tuapp.myapplication.data.repository.incomes

import android.util.Log
import com.tuapp.myapplication.data.database.dao.income.IncomeDao
import com.tuapp.myapplication.data.database.dao.user.UserDao
import com.tuapp.myapplication.data.database.entities.income.toDomain
import com.tuapp.myapplication.data.models.CommonResponseDomain
import com.tuapp.myapplication.data.models.incomes.request.CreateOrUpdateIncomeRequestDomain
import com.tuapp.myapplication.data.models.incomes.request.toRequest
import com.tuapp.myapplication.data.models.incomes.response.IngresoDetailsResponseDomain
import com.tuapp.myapplication.data.models.incomes.response.IngresoResponseDomain
import com.tuapp.myapplication.data.remote.ingresos.IncomesService
import com.tuapp.myapplication.data.remote.responses.incomesResponse.toDomain
import com.tuapp.myapplication.data.remote.responses.incomesResponse.toEntity
import com.tuapp.myapplication.data.remote.responses.toDomain
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

class IncomesRepositoryImpl(
    private val incomesService: IncomesService,
    private val incomeDao: IncomeDao,
    private val userDao: UserDao,
): IncomesRepository {

    override suspend fun getIncomesList(finanzaId: Int?): Flow<Resource<List<IngresoResponseDomain>>> = flow {
        emit(Resource.Loading)
        try {
            val incomesResponse = incomesService.getIncomesList(finanzaId)
            if(incomesResponse.ingresos.isNotEmpty()){
                incomeDao.clearIncomes(
                    finanzaId ?: getFinanceId(userDao)
                )
                incomeDao.insertIncomes(incomesResponse.toEntity())
            }else{
                emit(Resource.Success(emptyList()))
                return@flow
            }
        }catch (e :HttpException) {
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
            return@flow
        }catch (e: Exception) {
            Log.d("IncomesRepositoryImpl", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
            return@flow
        }

        val incomesList = incomeDao.getIncomesList(
            finanzaId ?: getFinanceId(userDao)
        ).map { entities ->
            val list = entities.map { it.toDomain() }

            Resource.Success(list)
        }.distinctUntilChanged()

        emitAll(incomesList)
    }.flowOn(Dispatchers.IO)

    override suspend fun getIncomeDetails(incomeId: Int): Flow<Resource<IngresoDetailsResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val incomeDetailsResponse = incomesService.getIncomeDetails(incomeId).ingreso

            emit(Resource.Success(incomeDetailsResponse.toDomain()))
        }catch (e :HttpException) {
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        }catch (e: Exception) {
            Log.d("IncomesRepositoryImpl", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun createIncome(
        finanzaId: Int?,
        createIncomeRequest: CreateOrUpdateIncomeRequestDomain
    ): Flow<Resource<CommonResponseDomain>> = flow{
        emit(Resource.Loading)
        try {
            val createIncomeRespose = incomesService.createIncome(finanzaId, createIncomeRequest.toRequest())

            emit(Resource.Success(createIncomeRespose.toDomain()))
        }catch (e :HttpException) {
            //ERRORES
            //400: BAD REQUEST, 500: ERROR DEL SERVER
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        }catch (e: Exception) {
            Log.d("IncomesRepositoryImpl", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateIncome(
        incomeId: Int,
        updateIncomeRequest: CreateOrUpdateIncomeRequestDomain
    ): Flow<Resource<CommonResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val updateRequest = incomesService.updateIncome(incomeId, updateIncomeRequest.toRequest())

            emit(Resource.Success(updateRequest.toDomain()))
        }catch (e :HttpException) {
            //ERRORES
            //400: BAD REQUEST, 404: NOT FOUND, 500: ERROR DEL SERVER
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        }catch (e: Exception) {
            Log.d("IncomesRepositoryImpl", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)
}