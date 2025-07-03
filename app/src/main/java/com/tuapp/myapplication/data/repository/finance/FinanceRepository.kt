package com.tuapp.myapplication.data.repository.finance

import com.tuapp.myapplication.data.models.CommonResponseDomain
import com.tuapp.myapplication.data.models.financeModels.request.CreateFinanceRequestDomain
import com.tuapp.myapplication.data.models.financeModels.request.JoinFinanceRequestDomain
import com.tuapp.myapplication.data.models.financeModels.response.CategorieResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.CreateInviteResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.DatoAnalisisDomain
import com.tuapp.myapplication.data.models.financeModels.response.FinanceDetailsResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.FinancesListResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.PrincipalFinanceResponseDomain
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {
    //ESAS FINANZA_ID ES DE LAS FINANZAS CONJUNTAS, ES NULL SI ES PERSONAL Y SI ES CONJUNTA SE PASA EL ID
    suspend fun getSummary(mes: Int, anio: Int, finanzaId: Int?): Flow<Resource<PrincipalFinanceResponseDomain>>
    suspend fun getData(mes: Int, anio: Int, finanzaId: Int?): Flow<Resource<List<CategorieResponseDomain>>>

    suspend fun createInvite(finanzaId: Int): Flow<Resource<CreateInviteResponseDomain>>
    suspend fun getFinancesList(): Flow<Resource<List<FinancesListResponseDomain>>>
    suspend fun getFinanceDetails(finanzaId: Int): Flow<Resource<FinanceDetailsResponseDomain>>
    suspend fun joinFinance(joinRequest: JoinFinanceRequestDomain): Flow<Resource<CommonResponseDomain>>
    suspend fun createFinance(createRequest: CreateFinanceRequestDomain): Flow<Resource<CommonResponseDomain>>
    suspend fun leaveFinance(finanzaId: Int): Flow<Resource<CommonResponseDomain>>
    suspend fun deleteFromFinance(userId: Int, finanzaId: Int): Flow<Resource<CommonResponseDomain>>
}