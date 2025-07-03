package com.tuapp.myapplication.data.database.dao.finance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuapp.myapplication.data.database.entities.finance.FinanceMembersEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceMembersDao {

    @Query("SELECT rolId FROM miembros_finanzas WHERE userId = :userId AND finanzaId = :finanzaId")
    fun getRole(userId: Int, finanzaId: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(membersList: List<FinanceMembersEntity>)

    @Query("DELETE FROM miembros_finanzas WHERE finanzaId = :finanzaId")
    suspend fun clearMembers(finanzaId: Int)
}