package com.tuapp.myapplication.data.models.subCategoryModels.response

data class SubCategoriesListResponseDomain(
    val lista_sub_categorias: List<ListaSubCategoriasDomain>,
    val success: Boolean,
)

data class ListaSubCategoriasDomain(
    val finanza_id: Int,
    val categoria_nombre: String,
    val nombre_usuario: String,
    val presupuesto: Double,
    val sub_categoria_id: Int,
    val sub_categoria_nombre: String,
    val tipo_gasto: String
)
