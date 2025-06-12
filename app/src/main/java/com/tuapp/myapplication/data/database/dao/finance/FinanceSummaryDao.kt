package com.tuapp.myapplication.data.database.dao.finance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuapp.myapplication.data.database.entities.finance.FinanceSummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceSummaryDao {

    @Query("SELECT * FROM resumen_financiero WHERE id = :finanza_id LIMIT 1")
    fun getSummary(finanza_id: Int): Flow<FinanceSummaryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummary(resumen: FinanceSummaryEntity)
}