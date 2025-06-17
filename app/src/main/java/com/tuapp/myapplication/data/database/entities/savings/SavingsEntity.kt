package com.tuapp.myapplication.data.database.entities.savings

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tuapp.myapplication.data.models.savingsModels.response.SavingsDataDomain

@Entity(tableName = "datos_ahorro")
data class SavingsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val finanzaId: Int,
    val anio: Int,
    val mes: Int,
    val meta_ahorro: Double,
    val monto_ahorrado: Double,
    val nombre_mes: String,
    val porcentaje_cumplimiento: Double
)

fun SavingsEntity.toDomain(): SavingsDataDomain {
   return SavingsDataDomain(
        anio,
        mes,
        meta_ahorro,
        monto_ahorrado,
        nombre_mes,
        porcentaje_cumplimiento
   )
}
