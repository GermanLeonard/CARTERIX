package com.tuapp.myapplication.data.models.transactionsModels.response

data class TransactionsOptionsDomain(
    val opciones: List<OpcionesResponseDomain>,
    val tipo_registro_id: Int,
    val tipo_registro_nombre: String
)

data class OpcionesResponseDomain(
    val id_opcion: Int,
    val nombre_opcion: String,
    val presupuesto_opcion: Double
)
