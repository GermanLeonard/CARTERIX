package com.tuapp.myapplication.data.remote.categories

import androidx.room.Query
import retrofit2.http.GET

interface CategoriesService {
    @GET("categoria/opciones")
    suspend fun getCategories(
        @Query("finanza_id") finanza_id: Int?
    )

    @GET("categoria/lista")
    suspend fun getCategoriesList(
        @Query("finanza_id") finanza_id: Int?
    )
}