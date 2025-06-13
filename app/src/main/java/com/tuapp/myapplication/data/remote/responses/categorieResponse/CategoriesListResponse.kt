package com.tuapp.myapplication.data.remote.responses.categorieResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.database.entities.category.CategoriaEgresoEntity

data class CategoriesListResponse(
    @SerializedName("lista_categorias")
    val lista_categorias: List<CategoriesList>,
    @SerializedName("finanza_id")
    val finanza_id: Int
)

data class CategoriesList(
    @SerializedName("categoria_id")
    val categoria_id: Int,
    @SerializedName("categoria_nombre")
    val categoria_nombre: String,
    @SerializedName("nombre_usuario")
    val nombre_usuario: String
)

fun CategoriesList.toEntity(finanza_id: Int): CategoriaEgresoEntity{
    return CategoriaEgresoEntity(
        categoriaId = categoria_id,
        finanzaId = finanza_id,
        nombreCategoria = categoria_nombre,
        usuarioRegistro = nombre_usuario,
    )
}

fun CategoriesListResponse.toEntity(): List<CategoriaEgresoEntity> {
    return this.lista_categorias.map {
        it.toEntity(this.finanza_id)
    }
}
