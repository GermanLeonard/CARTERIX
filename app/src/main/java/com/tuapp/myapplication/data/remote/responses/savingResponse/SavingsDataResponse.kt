package com.tuapp.myapplication.data.remote.responses.savingResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.database.entities.savings.SavingsEntity

data class SavingsDataResponse(
    @SerializedName("data")
    val data: List<SavingsData>,
    @SerializedName("finanza_id")
    val finanza_id: Int,
    @SerializedName("success")
    val success: Boolean
)

data class SavingsData(
    @SerializedName("anio")
    val anio: Int,
    @SerializedName("mes")
    val mes: Int,
    @SerializedName("meta_ahorro")
    val meta_ahorro: Double,
    @SerializedName("monto_ahorrado")
    val monto_ahorrado: Double,
    @SerializedName("nombre_mes")
    val nombre_mes: String,
    @SerializedName("porcentaje_cumplimiento")
    val porcentaje_cumplimiento: Double
)

fun SavingsData.toEntity(finanzaId: Int): SavingsEntity {
    return SavingsEntity(
        finanzaId = finanzaId,
        anio = anio,
        mes = mes,
        meta_ahorro = meta_ahorro,
        monto_ahorrado = monto_ahorrado,
        nombre_mes = nombre_mes,
        porcentaje_cumplimiento = porcentaje_cumplimiento
    )
}

fun SavingsDataResponse.toEntity(): List<SavingsEntity> {
    return data.map { it.toEntity(finanza_id) }
}