package com.tuapp.myapplication.data.remote.responses.subCategoriesResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.database.entities.subCategory.SubCategoriaEgresoEntity

data class SubCategoriesListResponse(
    @SerializedName("lista_sub_categorias")
    val lista_sub_categorias: List<ListaSubCategorias>,
    @SerializedName("success")
    val success: Boolean,
)

data class ListaSubCategorias(
    @SerializedName("finanza_id")
    val finanza_id: Int,
    @SerializedName("categoria_nombre")
    val categoria_nombre: String,
    @SerializedName("nombre_usuario")
    val nombre_usuario: String,
    @SerializedName("presupuesto")
    val presupuesto: Double,
    @SerializedName("sub_categoria_id")
    val sub_categoria_id: Int,
    @SerializedName("sub_categoria_nombre")
    val sub_categoria_nombre: String,
    @SerializedName("tipo_gasto")
    val tipo_gasto: String
)

fun ListaSubCategorias.toEntity(): SubCategoriaEgresoEntity {
    return SubCategoriaEgresoEntity(
        subCategoriaId = sub_categoria_id,
        finanzaId = finanza_id,
        subCategoriaNombre = sub_categoria_nombre,
        categoriaNombre = categoria_nombre,
        tipoGasto = tipo_gasto,
        nombreUsuario = nombre_usuario,
        presupuesto = presupuesto
    )
}

fun SubCategoriesListResponse.toEntity(): List<SubCategoriaEgresoEntity> {
    return lista_sub_categorias.map { it.toEntity() }
}