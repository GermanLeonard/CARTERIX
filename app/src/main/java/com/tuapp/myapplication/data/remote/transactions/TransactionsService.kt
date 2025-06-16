package com.tuapp.myapplication.data.remote.transactions

import com.tuapp.myapplication.data.remote.request.transactionRequest.CreateTransactionRequest
import com.tuapp.myapplication.data.remote.responses.CommonResponse
import com.tuapp.myapplication.data.remote.responses.transactionsResponse.TransactionDetailsResponse
import com.tuapp.myapplication.data.remote.responses.transactionsResponse.TransactionsListResponse
import com.tuapp.myapplication.data.remote.responses.transactionsResponse.TransactionsOptionsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionsService {
    @GET("transacciones/lista-transacciones")
    suspend fun getTransactionsList(
        @Query("finanza_id") finanzaId: Int?
    ): TransactionsListResponse

    @GET("transacciones/opciones-transaccion")
    suspend fun getTransactionsOptions(
        @Query("finanza_id") finanzaId: Int?
    ): TransactionsOptionsResponse

    @GET("transacciones/transaccion/{id}")
    suspend fun getTransactionsDetails(
        @Path("id") idTransaccion: Int
    ): TransactionDetailsResponse

    @POST("transacciones/crear")
    suspend fun createTransaction(
        @Query("finanza_id") finanzaId: Int?,
        @Body createTransactionRequest: CreateTransactionRequest
    ): CommonResponse
}
