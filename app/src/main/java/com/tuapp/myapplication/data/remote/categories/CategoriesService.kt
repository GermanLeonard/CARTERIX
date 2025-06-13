package com.tuapp.myapplication.data.remote.categories

import com.tuapp.myapplication.data.remote.request.categorieRequest.CreateOrUpdateCategorieRequest
import com.tuapp.myapplication.data.remote.responses.CommonResponse
import com.tuapp.myapplication.data.remote.responses.categorieResponse.CategorieDataResponse
import com.tuapp.myapplication.data.remote.responses.categorieResponse.CategoriesListResponse
import com.tuapp.myapplication.data.remote.responses.categorieResponse.CategoriesOptionsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoriesService {
    @GET("categoria/opciones")
    suspend fun getCategoriesOptions(
        @Query("finanza_id") finanzaId: Int?
    ): CategoriesOptionsResponse

    @GET("categoria/lista")
    suspend fun getCategoriesList(
        @Query("finanza_id") finanzaId: Int?
    ): CategoriesListResponse

    @GET("categoria/datos/{id}")
    suspend fun getCategorieData(
        @Path("id") id: Int,
        @Query("finanza_id") finanzaId: Int?
    ): CategorieDataResponse

    @POST("categoria/crear")
    suspend fun createCategorie(
        @Query("finanza_id") finanzaId: Int?,
        @Body createCategorieRequest: CreateOrUpdateCategorieRequest
    ): CommonResponse

    @POST("categoria/actualizar/{id}")
    suspend fun updateCategorie(
        @Path("id") id: Int,
        @Body updateCategorieRequest: CreateOrUpdateCategorieRequest
    ): CommonResponse
}