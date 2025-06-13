package com.tuapp.myapplication.data.remote.responses.financeResponse.summary

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.database.entities.finance.FinanceSummaryEntity


data class SummaryResponse(
    @SerializedName("finanza_principal")
    val finanza_principal: FinanzaPrincipalResponse? = null,
    @SerializedName("finanza_id")
    val finanza_id: Int,
    @SerializedName("success")
    val success: Boolean,
)

fun SummaryResponse.toEntity(): FinanceSummaryEntity {
    return FinanceSummaryEntity(
        id = finanza_id,
        acumulado = finanza_principal?.resumen_ahorros?.acumulado?.toDouble() ?: 0.0,
        meta = finanza_principal?.resumen_ahorros?.meta?.toDouble() ?: 0.0,
        progresoPorcentaje = finanza_principal?.resumen_ahorros?.progreso_porcentaje?.toDouble() ?: 0.0,
        consumoMensual = finanza_principal?.resumen_egresos?.consumo_mensual?.toDouble() ?: 0.0,
        presupuestoMensual = finanza_principal?.resumen_egresos?.presupuesto_mensual?.toDouble() ?: 0.0,
        variacionMensual = finanza_principal?.resumen_egresos?.variacion_mensual?.toDouble() ?: 0.0,
        diferencia = finanza_principal?.resumen_financiero?.diferencia?.toDouble() ?: 0.0,
        egresosTotales = finanza_principal?.resumen_financiero?.egresos_totales?.toDouble() ?: 0.0,
        ingresosTotales = finanza_principal?.resumen_financiero?.ingresos_totales?.toDouble() ?: 0.0
    )
}
