package com.tuapp.myapplication.data.repository.finance

import android.util.Log
import com.google.gson.Gson
import com.tuapp.myapplication.data.database.dao.finance.CategorieDataDao
import com.tuapp.myapplication.data.database.dao.finance.FinanceSummaryDao
import com.tuapp.myapplication.data.database.dao.user.UserDao
import com.tuapp.myapplication.data.database.entities.finance.toDomain
import com.tuapp.myapplication.data.models.financeModels.response.CategorieResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.PrincipalFinanceResponseDomain
import com.tuapp.myapplication.data.remote.finance.FinanceService
import com.tuapp.myapplication.data.remote.responses.CommonResponse
import com.tuapp.myapplication.data.remote.responses.financeResponse.data.toEntityList
import com.tuapp.myapplication.data.remote.responses.financeResponse.summary.toEntity
import com.tuapp.myapplication.helpers.Resource
import com.tuapp.myapplication.helpers.getFinanceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class FinanceRepositoryImpl(
    private val financeService: FinanceService,
    private val userDao: UserDao,
    private val financeSummaryDao: FinanceSummaryDao,
    private val categorieDataDao: CategorieDataDao,
): FinanceRepository {

    override suspend fun getSummary(mes: Int, anio: Int, finanzaId: Int?): Flow<Resource<PrincipalFinanceResponseDomain>> = flow {
        emit(Resource.Loading)
        try{
            val summaryResponse = financeService.getSummary(mes, anio, finanzaId)

            financeSummaryDao.insertSummary(summaryResponse.toEntity())
        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val gson = Gson()

            val errorResponse = gson.fromJson(errorBody, CommonResponse::class.java)
            val msg = errorResponse.message

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
                emit(Resource.Success(emptyList<CategorieResponseDomain>()))
            }
        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val gson = Gson()

            val errorResponse = gson.fromJson(errorBody, CommonResponse::class.java)
            val msg = errorResponse.message

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
}