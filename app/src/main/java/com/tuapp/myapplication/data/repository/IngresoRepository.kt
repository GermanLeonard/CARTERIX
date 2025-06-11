package com.tuapp.myapplication.data.repository

import com.tuapp.myapplication.data.database.dao.IngresoDao
import com.tuapp.myapplication.data.models.Ingreso
import kotlinx.coroutines.flow.Flow

class IngresoRepository(private val dao: IngresoDao) {
    val ingresos: Flow<List<Ingreso>> = dao.getAll()

    suspend fun insertar(ingreso: Ingreso) {
        dao.insert(ingreso)
    }

    suspend fun eliminar(ingreso: Ingreso) {
        dao.delete(ingreso)
    }
}
