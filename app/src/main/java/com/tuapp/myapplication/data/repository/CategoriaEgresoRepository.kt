package com.tuapp.myapplication.data.repository

import com.tuapp.myapplication.data.database.dao.category.CategoriaEgresoDao
import com.tuapp.myapplication.data.database.entities.category.CategoriaEgresoEntity
import kotlinx.coroutines.flow.Flow

class CategoriaEgresoRepository(private val dao: CategoriaEgresoDao) {

    val categorias: Flow<List<CategoriaEgresoEntity>> = dao.getCategories(1)

    suspend fun insertar(categoria: CategoriaEgresoEntity) {
    }

    suspend fun eliminar(categoria: CategoriaEgresoEntity) {
    }
}

