package com.tuapp.myapplication.data.repository.transactions

import android.util.Log
import com.tuapp.myapplication.data.database.dao.transactions.TransactionDao
import com.tuapp.myapplication.data.database.dao.user.UserDao
import com.tuapp.myapplication.data.database.entities.transactions.toDomain
import com.tuapp.myapplication.data.models.CommonResponseDomain
import com.tuapp.myapplication.data.models.transactionsModels.request.CreateTransactionDomain
import com.tuapp.myapplication.data.models.transactionsModels.request.toRequest
import com.tuapp.myapplication.data.models.transactionsModels.response.TransactionListResponseDomain
import com.tuapp.myapplication.data.models.transactionsModels.response.TransactionsDetailsDomain
import com.tuapp.myapplication.data.models.transactionsModels.response.TransactionsOptionsDomain
import com.tuapp.myapplication.data.remote.responses.toDomain
import com.tuapp.myapplication.data.remote.responses.transactionsResponse.toDomain
import com.tuapp.myapplication.data.remote.responses.transactionsResponse.toEntity
import com.tuapp.myapplication.data.remote.transactions.TransactionsService
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

class TransactionsRepositoryImpl(
    private val transactionsService: TransactionsService,
    private val transactionsDao: TransactionDao,
    private val userDao: UserDao,
): TransactionsRepository {

    override suspend fun getTransactionsList(mes: Int, anio: Int, finanzaId: Int?): Flow<Resource<List<TransactionListResponseDomain>>> = flow {
        emit(Resource.Loading)
        try {
            val transactionsResponse = transactionsService.getTransactionsList(mes, anio, finanzaId)
            if(transactionsResponse.transacciones.isNotEmpty()){
                transactionsDao.clearTransactions(
                    finanzaId ?: getFinanceId(userDao), mes, anio
                )
                transactionsDao.insertTransactions(transactionsResponse.toEntity())
            } else {
                emit(Resource.Success(emptyList()))
                return@flow
            }
        } catch (e: HttpException) {
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
            return@flow
        } catch (e: Exception) {
            Log.d("TransactionRepositoryImpl", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
            return@flow
        }

        val transactionList = transactionsDao.getTransactions(
            finanzaId ?: getFinanceId(userDao), mes, anio
        ).map { entities ->
            val transactions = entities.map { it.toDomain() }

            Resource.Success(transactions)
        }.distinctUntilChanged()
        emitAll(transactionList)
    }.flowOn(Dispatchers.IO)

    override suspend fun getTransactionDetails(idTransaccion: Int): Flow<Resource<TransactionsDetailsDomain>> = flow {
        emit(Resource.Loading)
        try {
            val transactionDetails = transactionsService.getTransactionsDetails(idTransaccion)

            emit(Resource.Success(transactionDetails.toDomain()))
        } catch (e: HttpException) {
            //ERRORES
            //404: NOT FOUND, 500: ERROR DEL SERVER
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        } catch (e: Exception) {
            Log.d("TransactionRepositoryImpl", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getTransactionOptions(finanzaId: Int?): Flow<Resource<List<TransactionsOptionsDomain>>> = flow {
        emit(Resource.Loading)
        try {
            val transactionsOptionsResponse = transactionsService.getTransactionsOptions(finanzaId)

            emit(Resource.Success(transactionsOptionsResponse.toDomain()))
        } catch (e: HttpException) {
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        } catch (e: Exception) {
            Log.d("TransactionRepositoryImpl", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun createTransaction(
        createTransactionRequest: CreateTransactionDomain,
        finanzaId: Int?
    ): Flow<Resource<CommonResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val createResponse = transactionsService.createTransaction(
                finanzaId,
                createTransactionRequest.toRequest()
            )
            emit(Resource.Success(createResponse.toDomain()))
        } catch (e: HttpException) {
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        } catch (e: Exception) {
            Log.d("TransactionRepositoryImpl", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)
}