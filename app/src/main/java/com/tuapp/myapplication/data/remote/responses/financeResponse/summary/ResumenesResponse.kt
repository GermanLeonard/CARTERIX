package com.tuapp.myapplication.data.remote.responses.financeResponse.summary

import com.google.gson.annotations.SerializedName

data class ResumenEgresosResponse(
    @SerializedName("consumo_mensual")
    val consumo_mensual: Double,
    @SerializedName("presupuesto_mensual")
    val presupuesto_mensual: Double,
    @SerializedName("variacion_mensual")
    val variacion_mensual: Double
)

data class ResumenAhorrosResponse(
    @SerializedName("acumulado")
    val acumulado: Double,
    @SerializedName("meta")
    val meta: Double,
    @SerializedName("progreso_porcentaje")
    val progreso_porcentaje: Double
)

data class ResumenFinancieroResponse(
    @SerializedName("diferencia")
    val diferencia: Double,
    @SerializedName("egresos_totales")
    val egresos_totales: Double,
    @SerializedName("ingresos_totales")
    val ingresos_totales: Double
)
