package com.tuapp.myapplication.data.database.entities.transactions

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tuapp.myapplication.data.models.transactionsModels.response.TransactionListResponseDomain

@Entity(tableName = "transacciones")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val finanzaId: Int,
    val transaccionId: Int,
    val nombreCategoria: String,
    val montoTransaccion: Double,
    val nombreUsuario: String,
    val tipoMovimientoId: Int,
    val tipoMovimientoNombre: String,
    val fechaTransaccion: String,
    val mes: Int,
    val anio: Int,
)

fun TransactionEntity.toDomain(): TransactionListResponseDomain {
    return TransactionListResponseDomain(
        fechaTransaccion,
        montoTransaccion,
        nombreCategoria,
        nombreUsuario,
        tipoMovimientoId,
        tipoMovimientoNombre,
        transaccionId
    )
}
