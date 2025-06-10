package com.tuapp.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tuapp.myapplication.data.models.Subcategoria
import kotlinx.coroutines.flow.Flow

@Dao
interface SubcategoriaDao {
    @Query("SELECT * FROM Subcategoria")
    fun getAll(): Flow<List<Subcategoria>>

    @Insert
    suspend fun insert(subcategoria: Subcategoria)

    @Delete
    suspend fun delete(subcategoria: Subcategoria)
}