package com.tuapp.myapplication.data.database.entities.finance

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "miembros_finanzas")
data class FinanceMembersEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val finanzaId: Int,
    val userId: Int,
    val nombreUsuario: String,
    //1 = ADMIN, 2 = COLABORADOR
    val rolId: Int,
)
