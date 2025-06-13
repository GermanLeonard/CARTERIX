package com.tuapp.myapplication.data.database.entities.subCategory

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tuapp.myapplication.data.models.subCategoryModels.response.OptionsDomain

@Entity(tableName = "tipo_gastos")
data class ExpensesTypesEntity (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val tipoNombre: String
)

fun ExpensesTypesEntity.toDomain(): OptionsDomain {
    return OptionsDomain(
        id,
        tipoNombre,
    )
}