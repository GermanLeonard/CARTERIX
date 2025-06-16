package com.tuapp.myapplication.data.database.entities.income

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tuapp.myapplication.data.models.incomesModels.response.IngresoResponseDomain

@Entity(tableName = "categoria_ingresos")
data class IncomesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val finanzaId: Int,
    val ingresoId: Int,
    val montoIngreso: Double,
    val nombreIngreso: String,
    val nombreUsuario: String
)

fun IncomesEntity.toDomain(): IngresoResponseDomain {
    return IngresoResponseDomain(
        ingresoId,
        montoIngreso,
        nombreIngreso,
        nombreUsuario
    )
}
