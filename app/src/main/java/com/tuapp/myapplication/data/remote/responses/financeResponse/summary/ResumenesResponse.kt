package com.tuapp.myapplication.data.remote.responses.financeResponse.summary

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.models.financeModels.ResumenAhorrosResponseDomain
import com.tuapp.myapplication.data.models.financeModels.ResumenEgresosResponseDomain
import com.tuapp.myapplication.data.models.financeModels.ResumenFinancieroResponseDomain

data class ResumenEgresosResponse(
    @SerializedName("consumo_mensual")
    val consumo_mensual: Float,
    @SerializedName("presupuesto_mensual")
    val presupuesto_mensual: Float,
    @SerializedName("variacion_mensual")
    val variacion_mensual: Float
)

data class ResumenAhorrosResponse(
    @SerializedName("acumulado")
    val acumulado: Float,
    @SerializedName("meta")
    val meta: Float,
    @SerializedName("progreso_porcentaje")
    val progreso_porcentaje: Float
)

data class ResumenFinancieroResponse(
    @SerializedName("diferencia")
    val diferencia: Float,
    @SerializedName("egresos_totales")
    val egresos_totales: Float,
    @SerializedName("ingresos_totales")
    val ingresos_totales: Float
)
