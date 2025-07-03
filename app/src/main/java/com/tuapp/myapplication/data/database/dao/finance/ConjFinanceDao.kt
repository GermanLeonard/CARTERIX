package com.tuapp.myapplication.data.database.dao.finance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuapp.myapplication.data.database.entities.finance.ConjFinanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConjFinanceDao {

    @Query("SELECT * FROM finanza_conjunta")
    fun getFinances(): Flow<List<ConjFinanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinances(finances: List<ConjFinanceEntity>)

    @Query("DELETE FROM finanza_conjunta")
    suspend fun removeFinances()
}