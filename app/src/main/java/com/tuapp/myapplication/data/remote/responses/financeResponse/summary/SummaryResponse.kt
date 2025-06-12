package com.tuapp.myapplication.data.remote.responses.financeResponse.summary

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.database.entities.finance.FinanceSummaryEntity
import com.tuapp.myapplication.data.models.financeModels.PrincipalFinanceResponseDomain
import com.tuapp.myapplication.data.models.financeModels.SummaryResponseDomain


data class SummaryResponse(
    @SerializedName("finanza_principal")
    val finanza_principal: FinanzaPrincipalResponse? = null,
    @SerializedName("finanza_id")
    val finanza_id: Int? = null,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String? = null
)

fun SummaryResponse.toEntity(): FinanceSummaryEntity {
    return FinanceSummaryEntity(
        id = finanza_id ?: 0,
        acumulado = finanza_principal?.resumen_ahorros?.acumulado?.toFloat() ?: 0f,
        meta = finanza_principal?.resumen_ahorros?.meta?.toFloat() ?: 0f,
        progresoPorcentaje = finanza_principal?.resumen_ahorros?.progreso_porcentaje?.toFloat() ?: 0f,
        consumoMensual = finanza_principal?.resumen_egresos?.consumo_mensual?.toFloat() ?: 0f,
        presupuestoMensual = finanza_principal?.resumen_egresos?.presupuesto_mensual?.toFloat() ?: 0f,
        variacionMensual = finanza_principal?.resumen_egresos?.variacion_mensual?.toFloat() ?: 0f,
        diferencia = finanza_principal?.resumen_financiero?.diferencia?.toFloat() ?: 0f,
        egresosTotales = finanza_principal?.resumen_financiero?.egresos_totales?.toFloat() ?: 0f,
        ingresosTotales = finanza_principal?.resumen_financiero?.ingresos_totales?.toFloat() ?: 0f
    )
}
