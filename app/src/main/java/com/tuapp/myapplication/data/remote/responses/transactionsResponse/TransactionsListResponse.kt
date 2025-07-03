package com.tuapp.myapplication.data.remote.responses.transactionsResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.database.entities.transactions.TransactionEntity

data class TransactionsListResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("finanza_id")
    val finanza_id: Int,
    @SerializedName("mes")
    val mes: Int,
    @SerializedName("anio")
    val anio: Int,
    @SerializedName("transacciones")
    val transacciones: List<TransaccionesLista>
)

data class TransaccionesLista(
    @SerializedName("fecha_transaccion")
    val fecha_transaccion: String,
    @SerializedName("monto_transaccion")
    val monto_transaccion: Double,
    @SerializedName("nombre_categoria")
    val nombre_categoria: String,
    @SerializedName("nombre_usuario")
    val nombre_usuario: String,
    @SerializedName("tipo_movimiento_id")
    val tipo_movimiento_id: Int,
    @SerializedName("tipo_movimiento_nombre")
    val tipo_movimiento_nombre: String,
    @SerializedName("transaccion_id")
    val transaccion_id: Int
)

fun TransaccionesLista.toEntity(finanzaId: Int, mes:Int, anio: Int): TransactionEntity {
    return TransactionEntity(
        finanzaId = finanzaId,
        transaccionId = transaccion_id,
        nombreCategoria = nombre_categoria,
        montoTransaccion = monto_transaccion,
        nombreUsuario = nombre_usuario,
        tipoMovimientoId = tipo_movimiento_id,
        tipoMovimientoNombre = tipo_movimiento_nombre,
        fechaTransaccion = fecha_transaccion,
        mes = mes,
        anio = anio,
    )
}

fun TransactionsListResponse.toEntity(): List<TransactionEntity> {
    return transacciones.map { it.toEntity(finanza_id, mes, anio) }
}