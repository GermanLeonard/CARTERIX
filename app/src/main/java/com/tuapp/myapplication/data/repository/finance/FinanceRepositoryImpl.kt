package com.tuapp.myapplication.data.repository.finance

import android.util.Log
import com.google.gson.Gson
import com.tuapp.myapplication.data.database.dao.finance.CategorieDataDao
import com.tuapp.myapplication.data.database.dao.finance.FinanceSummaryDao
import com.tuapp.myapplication.data.database.entities.finance.toDomain
import com.tuapp.myapplication.data.models.financeModels.DataResponseDomain
import com.tuapp.myapplication.data.models.financeModels.SummaryResponseDomain
import com.tuapp.myapplication.data.remote.finance.FinanceService
import com.tuapp.myapplication.data.remote.responses.financeResponse.data.DataResponse
import com.tuapp.myapplication.data.remote.responses.financeResponse.data.toEntityList
import com.tuapp.myapplication.data.remote.responses.financeResponse.summary.SummaryResponse
import com.tuapp.myapplication.data.remote.responses.financeResponse.summary.toEntity
import com.tuapp.myapplication.helpers.Resourse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class FinanceRepositoryImpl(
    private val financeService: FinanceService,
    private val financeSummaryDao: FinanceSummaryDao,
    private val categorieDataDao: CategorieDataDao,
): FinanceRepository {

    override suspend fun getSummary(mes: Int, anio: Int, finanza_id: Int?): Flow<Resourse<SummaryResponseDomain>> = flow {
        emit(Resourse.Loading)
        try{
            val summaryResponse = financeService.getSummary(mes, anio, finanza_id)

            if(summaryResponse.success){
                financeSummaryDao.insertSummary(summaryResponse.toEntity())
            }
        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val gson = Gson()

            val errorResponse = gson.fromJson(errorBody, SummaryResponse::class.java)
            val msg = errorResponse.message ?: "Ocurrio un error al traer el resumen"

            emit(Resourse.Error(message = msg))
            return@flow
        }catch (e: Exception){
            Log.d("FinanceRepository", "Error al hacer la petición: ${e.message}")
            emit(Resourse.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
            return@flow
        }

        val financeSummary = financeSummaryDao.getSummary().map { entity ->
            val summary = entity.toDomain()

            Resourse.Success(summary)
        }

        emitAll(financeSummary)
    }.flowOn(Dispatchers.IO)

    override suspend fun getData(mes: Int, anio: Int, finanza_id: Int?): Flow<Resourse<DataResponseDomain>> = flow {
        emit(Resourse.Loading)
        try{
            val dataResponse = financeService.getData(mes, anio, finanza_id)

            if(dataResponse.success){
                categorieDataDao.insertCategorias(dataResponse.toEntityList())
            }
        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val gson = Gson()

            val errorResponse = gson.fromJson(errorBody, DataResponse::class.java)
            val msg = errorResponse.message ?: "Ocurrio un error al traer los datos de la finanza"

            emit(Resourse.Error(message = msg))
            return@flow
        }catch (e: Exception){
            Log.d("FinanceRepository", "Error al hacer la petición: ${e.message}")
            emit(Resourse.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
            return@flow
        }

        val dataFinances = categorieDataDao.getCategorias().map { entities ->
            val categories = entities.map { it.toDomain() }

            if(categories.isEmpty()){
                    Resourse.Error(message = "No hay datos que mostrar")
            } else {
                Resourse.Success(DataResponseDomain(categories))
            }
        }

        emitAll(dataFinances)
    }.flowOn(Dispatchers.IO)
}