package com.tuapp.myapplication.data.database.dao.subCategory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuapp.myapplication.data.database.entities.subCategory.ExpensesTypesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesTypeDao {

    @Query("SELECT * FROM tipo_gastos")
    fun getExpensesTypes(): Flow<List<ExpensesTypesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTypes(types: List<ExpensesTypesEntity>)
}