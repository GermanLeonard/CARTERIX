package com.tuapp.myapplication.data.database.dao.subCategory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuapp.myapplication.data.database.entities.subCategory.SubCategoriaEgresoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubCategoryDao {

    @Query("SELECT * FROM sub_categoria_egresos WHERE finanzaId = :finanzaId")
    fun getSubCategories(finanzaId: Int): Flow<List<SubCategoriaEgresoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubCategories(subCategorias: List<SubCategoriaEgresoEntity>)

    @Query("DELETE FROM sub_categoria_egresos WHERE finanzaId = :finanzaId")
    suspend fun clearSubCategories(finanzaId: Int)
}