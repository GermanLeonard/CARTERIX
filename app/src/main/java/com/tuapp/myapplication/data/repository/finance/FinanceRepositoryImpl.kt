package com.tuapp.myapplication.data.repository.finance

import android.util.Log
import com.tuapp.myapplication.data.database.dao.finance.CategorieDataDao
import com.tuapp.myapplication.data.database.dao.finance.ConjFinanceDao
import com.tuapp.myapplication.data.database.dao.finance.FinanceMembersDao
import com.tuapp.myapplication.data.database.dao.finance.FinanceSummaryDao
import com.tuapp.myapplication.data.database.dao.user.UserDao
import com.tuapp.myapplication.data.database.entities.finance.toDomain
import com.tuapp.myapplication.data.models.CommonResponseDomain
import com.tuapp.myapplication.data.models.financeModels.request.CreateFinanceRequestDomain
import com.tuapp.myapplication.data.models.financeModels.request.JoinFinanceRequestDomain
import com.tuapp.myapplication.data.models.financeModels.request.toRequest
import com.tuapp.myapplication.data.models.financeModels.response.CategorieResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.CreateInviteResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.FinanceDetailsResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.FinancesListResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.PrincipalFinanceResponseDomain
import com.tuapp.myapplication.data.remote.finance.FinanceService
import com.tuapp.myapplication.data.remote.responses.financeResponse.conjunta.toDomain
import com.tuapp.myapplication.data.remote.responses.financeResponse.conjunta.toEntity
import com.tuapp.myapplication.data.remote.responses.financeResponse.data.toEntityList
import com.tuapp.myapplication.data.remote.responses.financeResponse.invitacion.toDomain
import com.tuapp.myapplication.data.remote.responses.financeResponse.summary.toEntity
import com.tuapp.myapplication.data.remote.responses.toDomain
import com.tuapp.myapplication.helpers.Resource
import com.tuapp.myapplication.helpers.errorParsing
import com.tuapp.myapplication.helpers.getFinanceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class FinanceRepositoryImpl(
    private val financeService: FinanceService,
    private val userDao: UserDao,
    private val financeMembersDao: FinanceMembersDao,
    private val conjFinanceDao: ConjFinanceDao,
    private val financeSummaryDao: FinanceSummaryDao,
    private val categorieDataDao: CategorieDataDao,
): FinanceRepository {

    //FUNCION PARA SABER EL ROL DEL USUARIO EL CUAL SERA USADO PARA MOSTRAR BOTONES O NO
    override suspend fun getRole(finanzaId: Int): Flow<Int> {
        val userId = userDao.getUser().map { it.id }.first()

        return financeMembersDao.getRole(userId, finanzaId)
    }

    override suspend fun getSummary(mes: Int, anio: Int, finanzaId: Int?): Flow<Resource<PrincipalFinanceResponseDomain>> = flow {
        emit(Resource.Loading)
        try{
            val summaryResponse = financeService.getSummary(mes, anio, finanzaId)

            financeSummaryDao.insertSummary(summaryResponse.toEntity())
        }catch (e: HttpException){
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
            return@flow
        }catch (e: Exception){
            Log.d("FinanceRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
            return@flow
        }

        val financeSummary = financeSummaryDao.getSummary(
            finanzaId ?: getFinanceId(userDao)
        ).map { entity ->
            val summary = entity.toDomain()
            Resource.Success(summary)
        }.distinctUntilChanged()

        emitAll(financeSummary)
    }.flowOn(Dispatchers.IO)

    override suspend fun getData(mes: Int, anio: Int, finanzaId: Int?): Flow<Resource<List<CategorieResponseDomain>>> = flow {
        emit(Resource.Loading)
        try{
            val dataResponse = financeService.getData(mes, anio, finanzaId)

            if(dataResponse.categorias.isNotEmpty()){
                categorieDataDao.clear(
                    finanzaId ?: getFinanceId(userDao)
                )
                categorieDataDao.insertCategorias(dataResponse.toEntityList())
            } else {
                emit(Resource.Success(emptyList()))
                return@flow
            }
        }catch (e: HttpException){
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
            return@flow
        }catch (e: Exception){
            Log.d("FinanceRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
            return@flow
        }

        val dataFinances = categorieDataDao.getCategorias(
            finanzaId ?: getFinanceId(userDao)
        ).map { entities ->
            val categories = entities.map { it.toDomain() }
                Resource.Success(categories)
        }.distinctUntilChanged()

        emitAll(dataFinances)
    }.flowOn(Dispatchers.IO)

    //FINANZA CONJUNTA

    override suspend fun createInvite(finanzaId: Int): Flow<Resource<CreateInviteResponseDomain>> = flow<Resource<CreateInviteResponseDomain>> {
        emit(Resource.Loading)
        try {
            val createInviteResponse = financeService.createInvite(finanzaId)

            emit(Resource.Success(createInviteResponse.toDomain()))
        } catch(e: HttpException) {
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        } catch(e: Exception) {
            Log.d("FinanceRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getFinancesList(): Flow<Resource<List<FinancesListResponseDomain>>> = flow {
        emit(Resource.Loading)
        try {
            val financeListResponse = financeService.getFinancesList()

            if(financeListResponse.finanzas.isNotEmpty()){
                conjFinanceDao.insertFinances(financeListResponse.toEntity())
            }else {
                emit(Resource.Success(emptyList()))
                return@flow
            }
        } catch(e: HttpException) {
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
            return@flow
        } catch(e: Exception) {
            Log.d("FinanceRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
            return@flow
        }

        val financeList = conjFinanceDao.getFinances().map { entities ->
            val list = entities.map { it.toDomain() }

            Resource.Success(list)
        }.distinctUntilChanged()
        emitAll(financeList)
    }.flowOn(Dispatchers.IO)

    override suspend fun getFinanceDetails(finanzaId: Int): Flow<Resource<FinanceDetailsResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val detailsResponse = financeService.getFinanceDetails(finanzaId)

            if(detailsResponse.detalles_finanza.finanza_miembros.isNotEmpty()){
                financeMembersDao.clearMembers(finanzaId)
                financeMembersDao.insertMembers(detailsResponse.toEntity())
            }
            emit(Resource.Success(detailsResponse.toDomain()))
        } catch(e: HttpException) {
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        } catch(e: Exception) {
            Log.d("FinanceRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun joinFinance(joinRequest: JoinFinanceRequestDomain): Flow<Resource<CommonResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val joinResponse = financeService.joinFinance(joinRequest.toRequest())

            emit(Resource.Success(joinResponse.toDomain()))
        } catch(e: HttpException) {
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        } catch(e: Exception) {
            Log.d("FinanceRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun createFinance(createRequest: CreateFinanceRequestDomain): Flow<Resource<CommonResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val createResponse = financeService.createFinance(createRequest.toRequest())

            emit(Resource.Success(createResponse.toDomain()))
        } catch(e: HttpException) {
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        } catch(e: Exception) {
            Log.d("FinanceRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun leaveFinance(finanzaId: Int): Flow<Resource<CommonResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val leaveResponse = financeService.leaveFinance(finanzaId)

            emit(Resource.Success(leaveResponse.toDomain()))
        } catch(e: HttpException) {
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        } catch(e: Exception) {
            Log.d("FinanceRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteFromFinance(
        userId: Int,
        finanzaId: Int
    ): Flow<Resource<CommonResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val deleteUserResponse = financeService.deleteFromFinance(userId, finanzaId)

            emit(Resource.Success(deleteUserResponse.toDomain()))
        } catch(e: HttpException) {
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        } catch(e: Exception) {
            Log.d("FinanceRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)
}