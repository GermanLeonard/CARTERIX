package com.tuapp.myapplication.data.remote.saving

import com.tuapp.myapplication.data.remote.request.savingRequest.CreateOrUpdateSavingGoal
import com.tuapp.myapplication.data.remote.responses.CommonResponse
import com.tuapp.myapplication.data.remote.responses.savingResponse.SavingsDataResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SavingService {
    @GET("ahorro/lista")
    suspend fun getSavingsData(
        @Query("anio") anio: Int,
        @Query("finanza_id") finanzaId: Int?,
    ): SavingsDataResponse

    @POST("ahorro/crear-meta")
    suspend fun createOrUpdateSavingGoal(
        @Query("finanza_id") finanzaId: Int?,
        @Body createOrUpdateSaving: CreateOrUpdateSavingGoal
    ): CommonResponse
}