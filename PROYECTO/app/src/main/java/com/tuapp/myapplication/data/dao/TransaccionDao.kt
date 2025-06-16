package com.tuapp.myapplication.data.dao

import androidx.room.*
import com.tuapp.myapplication.data.models.Transaccion
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaccionDao {
    @Query("SELECT * FROM transacciones")
    fun getAll(): Flow<List<Transaccion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaccion: Transaccion)

    @Delete
    suspend fun delete(transaccion: Transaccion)

    @Query("SELECT * FROM transacciones WHERE id = :id")
    fun getById(id: Int): Flow<Transaccion?>
}
