package com.tuapp.myapplication.data.remote.request.savingRequest

import com.google.gson.annotations.SerializedName

data class CreateOrUpdateSavingGoal(
    @SerializedName("monto")
    val monto: Double,
    @SerializedName("mes")
    val mes: Int,
    @SerializedName("anio")
    val anio: Int,
)
