package com.tuapp.myapplication.data.database.entities.finance

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tuapp.myapplication.data.models.financeModels.PrincipalFinanceResponseDomain
import com.tuapp.myapplication.data.models.financeModels.ResumenAhorrosResponseDomain
import com.tuapp.myapplication.data.models.financeModels.ResumenEgresosResponseDomain
import com.tuapp.myapplication.data.models.financeModels.ResumenFinancieroResponseDomain
import com.tuapp.myapplication.data.models.financeModels.SummaryResponseDomain

@Entity(tableName = "resumen_financiero")
data class FinanceSummaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val acumulado: Float,
    val meta: Float,
    val progresoPorcentaje: Float,

    val consumoMensual: Float,
    val presupuestoMensual: Float,
    val variacionMensual: Float,

    val diferencia: Float,
    val egresosTotales: Float,
    val ingresosTotales: Float
)

fun FinanceSummaryEntity.toDomain(): SummaryResponseDomain {
    val resumenAhorros = ResumenAhorrosResponseDomain(
        acumulado = this.acumulado,
        meta = this.meta,
        progreso_porcentaje = this.progresoPorcentaje
    )

    val resumenEgresos = ResumenEgresosResponseDomain(
        consumo_mensual = this.consumoMensual,
        presupuesto_mensual = this.presupuestoMensual,
        variacion_mensual = this.variacionMensual
    )

    val resumenFinanciero = ResumenFinancieroResponseDomain(
        diferencia = this.diferencia,
        egresos_totales = this.egresosTotales,
        ingresos_totales = this.ingresosTotales
    )

    val principal = PrincipalFinanceResponseDomain(
        resumen_ahorros = resumenAhorros,
        resumen_egresos = resumenEgresos,
        resumen_financiero = resumenFinanciero
    )

    return SummaryResponseDomain(
        finanza_principal = principal,
    )
}
