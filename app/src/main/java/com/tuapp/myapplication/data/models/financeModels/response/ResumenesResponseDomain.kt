package com.tuapp.myapplication.data.models.financeModels.response

data class ResumenAhorrosResponseDomain(
    val acumulado: Double,
    val meta: Double,
    val progreso_porcentaje: Double
)

data class ResumenEgresosResponseDomain(
    val consumo_mensual: Double,
    val presupuesto_mensual: Double,
    val variacion_mensual: Double
)

data class ResumenFinancieroResponseDomain(
    val diferencia: Double,
    val egresos_totales: Double,
    val ingresos_totales: Double
)
