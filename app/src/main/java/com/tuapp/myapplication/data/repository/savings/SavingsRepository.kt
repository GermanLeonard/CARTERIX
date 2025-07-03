package com.tuapp.myapplication.data.repository.savings

import com.tuapp.myapplication.data.models.CommonResponseDomain
import com.tuapp.myapplication.data.models.savingsModels.request.CreateOrUpdateSavingDomain
import com.tuapp.myapplication.data.models.savingsModels.response.SavingsDataDomain
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.Flow

interface SavingsRepository {
    suspend fun getSavingsData(finanzaId: Int?, anio: Int): Flow<Resource<List<SavingsDataDomain>>>
    suspend fun createOrUpdateSavings(finanzaId: Int?, createOrUpdateSaving: CreateOrUpdateSavingDomain): Flow<Resource<CommonResponseDomain>>
}