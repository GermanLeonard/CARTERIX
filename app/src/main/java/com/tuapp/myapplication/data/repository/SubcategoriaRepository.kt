package com.tuapp.myapplication.data.repository

import com.tuapp.myapplication.data.database.dao.SubcategoriaDao
import com.tuapp.myapplication.data.models.Subcategoria
import kotlinx.coroutines.flow.Flow

class SubcategoriaRepository(private val dao: SubcategoriaDao) {
    val subcategorias: Flow<List<Subcategoria>> = dao.getAll()

    suspend fun insertar(sub: Subcategoria) {
        dao.insert(sub)
    }

    suspend fun eliminar(sub: Subcategoria) {
        dao.delete(sub)
    }
}
