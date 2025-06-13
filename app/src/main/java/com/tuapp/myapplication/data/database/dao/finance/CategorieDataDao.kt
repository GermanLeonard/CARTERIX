package com.tuapp.myapplication.data.database.dao.finance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuapp.myapplication.data.database.entities.finance.CategorieDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategorieDataDao {

    @Query("SELECT * FROM categorias_data WHERE finanzaId = :finanzaId")
    fun getCategorias(finanzaId: Int): Flow<List<CategorieDataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategorias(categorias: List<CategorieDataEntity>)

    @Query("DELETE FROM categorias_data WHERE finanzaId = :finanzaId")
    suspend fun clear(finanzaId: Int)
}