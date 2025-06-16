package com.tuapp.myapplication.data.database.dao.income

import androidx.room.*
import com.tuapp.myapplication.data.database.entities.income.IncomesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {
    @Query("SELECT * FROM categoria_ingresos WHERE finanzaId = :finanzaId")
    fun getIncomesList(finanzaId: Int): Flow<List<IncomesEntity>>

    @Insert
    suspend fun insertIncomes(ingresos: List<IncomesEntity>)

    @Query("DELETE FROM categoria_ingresos WHERE finanzaId = :finanzaId")
    suspend fun clearIncomes(finanzaId: Int)
}
