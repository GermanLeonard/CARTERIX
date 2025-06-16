package com.tuapp.myapplication.data.repository

import com.tuapp.myapplication.data.dao.TransaccionDao
import com.tuapp.myapplication.data.models.Transaccion
import kotlinx.coroutines.flow.Flow

class TransaccionRepository(private val dao: TransaccionDao) {
    val transacciones: Flow<List<Transaccion>> = dao.getAll()

    suspend fun insertar(transaccion: Transaccion) {
        dao.insert(transaccion)
    }

    suspend fun eliminar(transaccion: Transaccion) {
        dao.delete(transaccion)
    }

    fun obtenerPorId(id: Int): Flow<Transaccion?> {
        return dao.getById(id)
    }

}
