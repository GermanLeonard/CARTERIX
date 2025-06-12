package com.tuapp.myapplication.data.models.financeModels

data class PrincipalFinanceResponseDomain(
    val resumen_ahorros: ResumenAhorrosResponseDomain,
    val resumen_egresos: ResumenEgresosResponseDomain,
    val resumen_financiero: ResumenFinancieroResponseDomain
)

