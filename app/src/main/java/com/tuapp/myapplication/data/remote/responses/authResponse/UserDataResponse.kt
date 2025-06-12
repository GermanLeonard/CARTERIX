package com.tuapp.myapplication.data.remote.responses.authResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.database.entities.UserEntity

data class UserDataResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("correo")
    val correo: String
)

fun UserDataResponse.toEntity(): UserEntity {
    return UserEntity(
        id,
        nombre,
        correo
    )
}
