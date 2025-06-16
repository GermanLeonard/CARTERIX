package com.tuapp.myapplication.data.remote.responses.subCategoriesResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.database.entities.subCategory.ExpensesTypesEntity
import com.tuapp.myapplication.data.models.subCategoryModels.response.OptionsDomain

data class ExpensesTypeResponse (
    @SerializedName("opciones")
    val opciones: List<Options>
)

data class Options(
    @SerializedName("tipo_id")
    val tipo_id: Int,
    @SerializedName("tipo_nombre")
    val tipo_nombre: String
)

fun Options.toEntity(): ExpensesTypesEntity {
    return ExpensesTypesEntity(
        tipo_id,
        tipo_nombre
    )
}

fun ExpensesTypeResponse.toEntity(): List<ExpensesTypesEntity> {
    return opciones.map { it.toEntity() }
}


fun Options.toDomain(): OptionsDomain {
    return OptionsDomain(
        tipo_id,
        tipo_nombre
    )
}

fun ExpensesTypeResponse.toDomain(): List<OptionsDomain> {
    return opciones.map { it.toDomain() }
}