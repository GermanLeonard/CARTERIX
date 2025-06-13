package com.tuapp.myapplication.data.repository.finance

import com.tuapp.myapplication.data.models.financeModels.response.CategorieResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.PrincipalFinanceResponseDomain
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {
    //ESAS FINANZA_ID ES DE LAS FINANZAS CONJUNTAS, ES NULL SI ES PERSONAL Y SI ES CONJUNTA SE PASA EL ID
    suspend fun getSummary(mes: Int, anio: Int, finanzaId: Int?): Flow<Resource<PrincipalFinanceResponseDomain>>
    suspend fun getData(mes: Int, anio: Int, finanzaId: Int?): Flow<Resource<List<CategorieResponseDomain>>>
}