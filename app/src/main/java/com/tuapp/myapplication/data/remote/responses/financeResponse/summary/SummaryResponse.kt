package com.tuapp.myapplication.data.remote.responses.financeResponse.summary

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.database.entities.finance.FinanceSummaryEntity
import com.tuapp.myapplication.data.models.financeModels.PrincipalFinanceResponseDomain
import com.tuapp.myapplication.data.models.financeModels.SummaryResponseDomain


data class SummaryResponse(
    @SerializedName("finanza_principal")
    val finanza_principal: FinanzaPrincipalResponse,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String? = null
)

fun SummaryResponse.toEntity(): FinanceSummaryEntity{
    return FinanceSummaryEntity(
        acumulado = finanza_principal.resumen_ahorros.acumulado,
        meta = finanza_principal.resumen_ahorros.meta,
        progresoPorcentaje = finanza_principal.resumen_ahorros.progreso_porcentaje,
        consumoMensual = finanza_principal.resumen_egresos.consumo_mensual,
        presupuestoMensual = finanza_principal.resumen_egresos.presupuesto_mensual,
        variacionMensual = finanza_principal.resumen_egresos.variacion_mensual,
        diferencia = finanza_principal.resumen_financiero.diferencia,
        egresosTotales = finanza_principal.resumen_financiero.egresos_totales,
        ingresosTotales = finanza_principal.resumen_financiero.ingresos_totales,
    )
}
