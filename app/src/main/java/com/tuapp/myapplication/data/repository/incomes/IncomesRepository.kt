package com.tuapp.myapplication.data.repository.incomes

import com.tuapp.myapplication.data.models.CommonResponseDomain
import com.tuapp.myapplication.data.models.incomesModels.request.CreateOrUpdateIncomeRequestDomain
import com.tuapp.myapplication.data.models.incomesModels.response.IngresoDetailsResponseDomain
import com.tuapp.myapplication.data.models.incomesModels.response.IngresoResponseDomain
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.Flow

interface IncomesRepository {
    suspend fun getIncomesList(finanzaId: Int?): Flow<Resource<List<IngresoResponseDomain>>>
    suspend fun getIncomeDetails(incomeId: Int): Flow<Resource<IngresoDetailsResponseDomain>>
    suspend fun createIncome(finanzaId: Int?, createIncomeRequest: CreateOrUpdateIncomeRequestDomain): Flow<Resource<CommonResponseDomain>>
    suspend fun updateIncome(incomeId: Int, updateIncomeRequest: CreateOrUpdateIncomeRequestDomain): Flow<Resource<CommonResponseDomain>>
}