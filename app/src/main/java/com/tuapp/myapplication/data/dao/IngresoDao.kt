package com.tuapp.myapplication.data.dao

import androidx.room.*
import com.tuapp.myapplication.data.models.Ingreso
import kotlinx.coroutines.flow.Flow

@Dao
interface IngresoDao {
    @Query("SELECT * FROM Ingreso")
    fun getAll(): Flow<List<Ingreso>>

    @Insert
    suspend fun insert(ingreso: Ingreso)

    @Delete
    suspend fun delete(ingreso: Ingreso)
}
