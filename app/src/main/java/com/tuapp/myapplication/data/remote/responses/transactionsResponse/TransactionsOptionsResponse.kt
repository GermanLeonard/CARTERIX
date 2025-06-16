package com.tuapp.myapplication.data.remote.responses.transactionsResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.models.transactionsModels.response.OpcionesResponseDomain
import com.tuapp.myapplication.data.models.transactionsModels.response.TransactionsOptionsDomain

class TransactionsOptionsResponse : ArrayList<TransactionsOptionsResponseItem>()

data class TransactionsOptionsResponseItem(
    @SerializedName("opciones")
    val opciones: List<OpcionesResponse>,
    @SerializedName("tipo_registro_id")
    val tipo_registro_id: Int,
    @SerializedName("tipo_registro_nombre")
    val tipo_registro_nombre: String
)

data class OpcionesResponse(
    @SerializedName("id_opcion")
    val id_opcion: Int,
    @SerializedName("nombre_opcion")
    val nombre_opcion: String,
    @SerializedName("presupuesto_opcion")
    val presupuesto_opcion: Double
)

fun OpcionesResponse.toDomain(): OpcionesResponseDomain {
    return OpcionesResponseDomain(
        id_opcion,
        nombre_opcion,
        presupuesto_opcion
    )
}

fun TransactionsOptionsResponseItem.toDomain(): TransactionsOptionsDomain {
    return TransactionsOptionsDomain(
        opciones = opciones.map { it.toDomain() },
        tipo_registro_id,
        tipo_registro_nombre
    )
}

fun TransactionsOptionsResponse.toDomain(): List<TransactionsOptionsDomain> {
    return this.map { it.toDomain() }
}