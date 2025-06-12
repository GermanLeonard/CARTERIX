package com.tuapp.myapplication.data.database.entities.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tuapp.myapplication.data.models.authModels.UserDataDomain

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val nombre: String,
    val correo: String,
)

fun UserEntity.toDomain(): UserDataDomain {
   return UserDataDomain(
       id,
       nombre,
       correo
   )
}
