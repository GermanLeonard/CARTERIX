package com.tuapp.myapplication.data.models.financeModels.response

data class FinanceDetailsResponseDomain(
    val finanza_descripcion: String,
    val finanza_miembros: List<FinanzaMiembroDomain>,
    val finanza_titulo: String
)

data class FinanzaMiembroDomain(
    val id_usuario: Int,
    val nombre_usuario: String,
    val rol_usuario: Int
)
