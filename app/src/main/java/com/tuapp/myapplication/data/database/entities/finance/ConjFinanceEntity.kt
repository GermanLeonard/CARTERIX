package com.tuapp.myapplication.data.database.entities.finance

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tuapp.myapplication.data.models.financeModels.response.FinancesListResponseDomain

@Entity(tableName = "finanza_conjunta")
data class ConjFinanceEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val tituloFinanza: String,
    val nombreAdmin: String
)

fun ConjFinanceEntity.toDomain(): FinancesListResponseDomain {
    return FinancesListResponseDomain(
        id,
        tituloFinanza,
        nombreAdmin
    )
}
