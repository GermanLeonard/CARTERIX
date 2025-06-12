package com.tuapp.myapplication.data.repository.finance

import com.tuapp.myapplication.data.models.financeModels.DataResponseDomain
import com.tuapp.myapplication.data.models.financeModels.SummaryResponseDomain
import com.tuapp.myapplication.helpers.Resourse
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {
    //ESAS FINANZA_ID ES DE LAS FINANZAS CONJUNTAS, ES NULL SI ES PERSONAL Y SI ES CONJUNTA SE PASA EL ID
    suspend fun getSummary(mes: Int, anio: Int, finanza_id: Int? = null): Flow<Resourse<SummaryResponseDomain>>
    suspend fun getData(mes: Int, anio: Int, finanza_id: Int? = null): Flow<Resourse<DataResponseDomain>>
}