package com.tuapp.myapplication.data.database.dao.saving

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuapp.myapplication.data.database.entities.savings.SavingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsDao {

    @Query("SELECT * FROM datos_ahorro WHERE finanzaId = :finanzaId")
    fun getSavingsData(finanzaId: Int): Flow<List<SavingsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavingData(savingData: List<SavingsEntity>)

    @Query("DELETE FROM datos_ahorro WHERE finanzaId = :finanzaId")
    suspend fun clearSavingData(finanzaId: Int)
}