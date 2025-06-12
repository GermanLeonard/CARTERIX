package com.tuapp.myapplication.data.repository

import com.tuapp.myapplication.data.database.dao.CategoriaEgresoDao
import com.tuapp.myapplication.data.models.CategoriaEgreso
import kotlinx.coroutines.flow.Flow

class CategoriaEgresoRepository(private val dao: CategoriaEgresoDao) {

    val categorias: Flow<List<CategoriaEgreso>> = dao.getAll()

    suspend fun insertar(categoria: CategoriaEgreso) {
        dao.insert(categoria)
    }

    suspend fun eliminar(categoria: CategoriaEgreso) {
        dao.delete(categoria)
    }
}

