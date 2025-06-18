package com.tuapp.myapplication.data.database.dao.transactions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuapp.myapplication.data.database.entities.transactions.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transacciones WHERE finanzaId = :finanzaId AND mes = :mes AND anio = :anio")
    fun getTransactions(finanzaId: Int, mes: Int, anio: Int): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Query("DELETE FROM transacciones WHERE finanzaId = :finanzaId AND mes = :mes AND anio = :anio")
    suspend fun clearTransactions(finanzaId: Int, mes: Int, anio: Int)
}