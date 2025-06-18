package com.tuapp.myapplication.data.repository.transactions

import com.tuapp.myapplication.data.models.CommonResponseDomain
import com.tuapp.myapplication.data.models.transactionsModels.request.CreateTransactionDomain
import com.tuapp.myapplication.data.models.transactionsModels.response.TransactionListResponseDomain
import com.tuapp.myapplication.data.models.transactionsModels.response.TransactionsDetailsDomain
import com.tuapp.myapplication.data.models.transactionsModels.response.TransactionsOptionsDomain
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {
    suspend fun getTransactionsList(mes: Int, anio: Int, finanzaId: Int?): Flow<Resource<List<TransactionListResponseDomain>>>
    suspend fun getTransactionOptions(finanzaId: Int?): Flow<Resource<List<TransactionsOptionsDomain>>>
    suspend fun getTransactionDetails(idTransaccion: Int): Flow<Resource<TransactionsDetailsDomain>>
    suspend fun createTransaction(
        createTransactionRequest: CreateTransactionDomain,
        finanzaId: Int?
    ): Flow<Resource<CommonResponseDomain>>
}