package com.tuapp.myapplication.data.database.dao.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuapp.myapplication.data.database.entities.category.CategoriaEgresoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaEgresoDao {
    @Query("SELECT * FROM categorias_egreso WHERE finanzaId = :finanzaId")
    fun getCategories(finanzaId: Int): Flow<List<CategoriaEgresoEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCategories(categoria: List<CategoriaEgresoEntity>)

    @Query("DELETE FROM categorias_egreso WHERE finanzaId = :finanzaId")
    suspend fun clearCategories(finanzaId: Int)
}