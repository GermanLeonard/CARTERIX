package com.tuapp.myapplication.data.remote.responses.financeResponse.conjunta

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.database.entities.finance.FinanceMembersEntity
import com.tuapp.myapplication.data.models.financeModels.response.FinanceDetailsResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.FinanzaMiembroDomain

data class FinanceDetailsResponse(
    @SerializedName("detalles_finanza")
    val detalles_finanza: DetallesFinanza,
    @SerializedName("finanza_id")
    val finanza_id: Int,
    @SerializedName("success")
    val success: Boolean
)

data class DetallesFinanza(
    @SerializedName("finanza_descripcion")
    val finanza_descripcion: String,
    @SerializedName("finanza_miembros")
    val finanza_miembros: List<FinanzaMiembro>,
    @SerializedName("finanza_titulo")
    val finanza_titulo: String
)

data class FinanzaMiembro(
    @SerializedName("id_usuario")
    val id_usuario: Int,
    @SerializedName("nombre_usuario")
    val nombre_usuario: String,
    @SerializedName("rol_usuario")
    val rol_usuario: Int
)

fun FinanzaMiembro.toEntity(finanzaId: Int): FinanceMembersEntity {
    return FinanceMembersEntity(
        finanzaId = finanzaId,
        userId = id_usuario,
        nombreUsuario = nombre_usuario,
        rolId = rol_usuario
    )
}


fun FinanzaMiembro.toDomain(): FinanzaMiembroDomain {
    return FinanzaMiembroDomain(
        id_usuario,
        nombre_usuario,
        rol_usuario
    )
}

fun FinanceDetailsResponse.toDomain(): FinanceDetailsResponseDomain {
    return FinanceDetailsResponseDomain(
        detalles_finanza.finanza_descripcion,
        detalles_finanza.finanza_miembros.map { it.toDomain() },
        detalles_finanza.finanza_titulo
    )
}

fun FinanceDetailsResponse.toEntity(): List<FinanceMembersEntity> {
    return detalles_finanza.finanza_miembros.map { it.toEntity(finanza_id) }
}

