package com.tuapp.myapplication.data.remote.finance

import com.tuapp.myapplication.data.remote.request.financesRequest.CreateFinanceRequest
import com.tuapp.myapplication.data.remote.request.financesRequest.JoinFinanceRequest
import com.tuapp.myapplication.data.remote.responses.CommonResponse
import com.tuapp.myapplication.data.remote.responses.financeResponse.conjunta.FinanceDetailsResponse
import com.tuapp.myapplication.data.remote.responses.financeResponse.conjunta.FinanceListRespose
import com.tuapp.myapplication.data.remote.responses.financeResponse.data.DataResponse
import com.tuapp.myapplication.data.remote.responses.financeResponse.invitacion.CreateInviteResponse
import com.tuapp.myapplication.data.remote.responses.financeResponse.summary.SummaryResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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

    //FINANZAS CONJUNTAS
    @POST("invitaciones/crear/{id}")
    suspend fun createInvite(
       @Path("id") finanzaId: Int,
    ): CreateInviteResponse

    @GET("finanza-conjunta/lista")
    suspend fun getFinancesList(): FinanceListRespose

    @GET("finanza-conjunta/detalles/{id}")
    suspend fun getFinanceDetails(
        @Path("id") finanzaId: Int
    ): FinanceDetailsResponse

    @POST("finanza-conjunta/unirse")
    suspend fun joinFinance(
        @Body joinRequest: JoinFinanceRequest
    ): CommonResponse

    @POST("finanza-conjunta/crear")
    suspend fun createFinance(
        @Body createFinanceRequest: CreateFinanceRequest
    ): CommonResponse

    @DELETE("finanza-conjunta/salir/{id}")
    suspend fun leaveFinance(
        @Path("id") finanzaId: Int
    ): CommonResponse

    @DELETE("finanza-conjunta/user/{id}")
    suspend fun deleteFromFinance(
        @Path("id") userId: Int,
        @Query("finanza_id") finanzaId: Int
    ): CommonResponse
}