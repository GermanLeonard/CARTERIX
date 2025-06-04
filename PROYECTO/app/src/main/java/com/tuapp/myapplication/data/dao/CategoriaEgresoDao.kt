package com.tuapp.myapplication.data.dao

import androidx.room.*
import com.tuapp.myapplication.data.models.CategoriaEgreso
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaEgresoDao {
    @Query("SELECT * FROM categorias_egreso")
    fun getAll(): Flow<List<CategoriaEgreso>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoria: CategoriaEgreso)

    @Delete
    suspend fun delete(categoria: CategoriaEgreso)
}

