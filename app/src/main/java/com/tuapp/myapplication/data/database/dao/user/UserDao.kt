package com.tuapp.myapplication.data.database.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuapp.myapplication.data.database.entities.user.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): Flow<UserEntity?>

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getUserOnce(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM user")
    suspend fun clearUser()
}