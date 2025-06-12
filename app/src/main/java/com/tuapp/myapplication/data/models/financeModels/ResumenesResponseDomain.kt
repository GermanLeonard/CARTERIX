package com.tuapp.myapplication.data.models.financeModels

data class ResumenAhorrosResponseDomain(
    val acumulado: Float,
    val meta: Float,
    val progreso_porcentaje: Float
)

data class ResumenEgresosResponseDomain(
    val consumo_mensual: Float,
    val presupuesto_mensual: Float,
    val variacion_mensual: Float
)

data class ResumenFinancieroResponseDomain(
    val diferencia: Float,
    val egresos_totales: Float,
    val ingresos_totales: Float
)
