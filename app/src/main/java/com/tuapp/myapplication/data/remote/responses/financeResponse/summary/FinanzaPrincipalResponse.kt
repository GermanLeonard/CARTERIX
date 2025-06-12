package com.tuapp.myapplication.data.remote.responses.financeResponse.summary

import com.google.gson.annotations.SerializedName

data class FinanzaPrincipalResponse(
    @SerializedName("resumen_ahorros")
    val resumen_ahorros: ResumenAhorrosResponse,
    @SerializedName("resumen_egresos")
    val resumen_egresos: ResumenEgresosResponse,
    @SerializedName("resumen_financiero")
    val resumen_financiero: ResumenFinancieroResponse
)
