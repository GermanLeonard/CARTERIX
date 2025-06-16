package com.tuapp.myapplication.data.remote.ingresos

import com.tuapp.myapplication.data.remote.request.incomesRequest.CreateOrUpdateIncomeRequest
import com.tuapp.myapplication.data.remote.responses.CommonResponse
import com.tuapp.myapplication.data.remote.responses.incomesResponse.IncomeDetailsResponse
import com.tuapp.myapplication.data.remote.responses.incomesResponse.IncomesListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface IncomesService {
    @GET("ingresos/lista")
    suspend fun getIncomesList(
        @Query("finanza_id") finanzaId: Int?,
    ): IncomesListResponse

    @GET("ingresos/ingreso/{id}")
    suspend fun getIncomeDetails(
        @Path("id") ingresoId: Int
    ): IncomeDetailsResponse

    @POST("ingresos/crear")
    suspend fun createIncome(
        @Query("finanza_id") finanzaId: Int?,
        @Body createIncomeRequest: CreateOrUpdateIncomeRequest,
    ): CommonResponse

    @PUT("ingresos/actualizar/{id}")
    suspend fun updateIncome(
        @Path("id") ingresoId: Int,
        @Body updateIncomeRequest: CreateOrUpdateIncomeRequest,
    ): CommonResponse

}