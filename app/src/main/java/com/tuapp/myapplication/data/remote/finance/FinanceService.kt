package com.tuapp.myapplication.data.remote.finance

import com.tuapp.myapplication.data.remote.responses.financeResponse.data.DataResponse
import com.tuapp.myapplication.data.remote.responses.financeResponse.summary.SummaryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FinanceService {
    @GET("finanza/resumen")
    suspend fun getSummary(
        @Query("mes") mes: Int,
        @Query("anio") anio: Int,
        @Query("finanza_id") finanzaId: Int?,
    ): SummaryResponse

    @GET("finanza/datos")
    suspend fun getData(
        @Query("mes") mes: Int,
        @Query("anio") anio: Int,
        @Query("finanza_id") finanzaId: Int?,
    ): DataResponse
}