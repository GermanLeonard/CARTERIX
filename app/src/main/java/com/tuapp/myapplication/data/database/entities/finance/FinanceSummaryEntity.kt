package com.tuapp.myapplication.data.database.entities.finance

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tuapp.myapplication.data.models.financeModels.response.PrincipalFinanceResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.ResumenAhorrosResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.ResumenEgresosResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.ResumenFinancieroResponseDomain

@Entity(tableName = "resumen_financiero")
data class FinanceSummaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val acumulado: Double,
    val meta: Double,
    val progresoPorcentaje: Double,

    val consumoMensual: Double,
    val presupuestoMensual: Double,
    val variacionMensual: Double,

    val diferencia: Double,
    val egresosTotales: Double,
    val ingresosTotales: Double
)

fun FinanceSummaryEntity.toDomain(): PrincipalFinanceResponseDomain {
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

    return principal
}
