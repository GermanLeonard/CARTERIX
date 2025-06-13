package com.tuapp.myapplication.data.remote.subCategories

import com.tuapp.myapplication.data.remote.request.subCategoriesRequest.CreateOrUpdateSubCategoryRequest
import com.tuapp.myapplication.data.remote.responses.CommonResponse
import com.tuapp.myapplication.data.remote.responses.subCategoriesResponse.ExpensesTypeResponse
import com.tuapp.myapplication.data.remote.responses.subCategoriesResponse.SubCategoriesListResponse
import com.tuapp.myapplication.data.remote.responses.subCategoriesResponse.SubCategoryDetailsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SubCategoriesService {
    @GET("sub-categoria/lista")
    suspend fun getSubCategoriesList(
        @Query("finanza_id") finanzaId: Int?
    ): SubCategoriesListResponse

    @GET("sub-categoria/categoria/{id}")
    suspend fun getSubCategoryDetails(
        @Path("id") subCategoryId: Int
    ): SubCategoryDetailsResponse

    @GET("sub-categoria/opciones-gasto")
    suspend fun getExpensesOptions(): ExpensesTypeResponse

    @POST("sub-categoria/crear")
    suspend fun createSubCategory(
        @Query("finanza_id") finanzaId: Int?,
        @Body createSubCategory: CreateOrUpdateSubCategoryRequest
    ): CommonResponse

    @PUT("sub-categoria/actualizar/{id}")
    suspend fun updateSubCategory(
        @Path("id") subCategoryId: Int,
        @Body updateSubCategory: CreateOrUpdateSubCategoryRequest
    ): CommonResponse
}