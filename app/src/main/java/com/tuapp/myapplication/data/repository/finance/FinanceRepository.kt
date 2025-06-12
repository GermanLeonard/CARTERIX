package com.tuapp.myapplication.data.repository.finance

import com.tuapp.myapplication.data.models.financeModels.DataResponseDomain
import com.tuapp.myapplication.data.models.financeModels.SummaryResponseDomain
import com.tuapp.myapplication.helpers.Resourse
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {

    suspend fun getSummary(mes: Int, anio: Int, finanza_id: Int? = null): Flow<Resourse<SummaryResponseDomain>>
    suspend fun getData(mes: Int, anio: Int, finanza_id: Int? = null): Flow<Resourse<DataResponseDomain>>
}