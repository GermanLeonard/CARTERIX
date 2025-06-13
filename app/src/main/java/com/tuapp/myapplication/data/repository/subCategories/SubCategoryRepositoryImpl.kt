package com.tuapp.myapplication.data.repository.subCategories

import android.util.Log
import com.google.gson.Gson
import com.tuapp.myapplication.data.database.dao.subCategory.ExpensesTypeDao
import com.tuapp.myapplication.data.database.dao.subCategory.SubCategoryDao
import com.tuapp.myapplication.data.database.dao.user.UserDao
import com.tuapp.myapplication.data.database.entities.subCategory.toDomain
import com.tuapp.myapplication.data.models.CommonResponseDomain
import com.tuapp.myapplication.data.models.subCategoryModels.response.ListaSubCategoriasDomain
import com.tuapp.myapplication.data.models.subCategoryModels.response.OptionsDomain
import com.tuapp.myapplication.data.models.subCategoryModels.response.SubCategoriaDomain
import com.tuapp.myapplication.data.remote.responses.CommonResponse
import com.tuapp.myapplication.data.remote.responses.subCategoriesResponse.toDomain
import com.tuapp.myapplication.data.remote.responses.subCategoriesResponse.toEntity
import com.tuapp.myapplication.data.remote.subCategories.SubCategoriesService
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

class SubCategoryRepositoryImpl(
    private val subCategoryService: SubCategoriesService,
    private val subCategoryDao: SubCategoryDao,
    private val expensesTypesDao: ExpensesTypeDao,
    private val userDao: UserDao,
): SubCategoryRepository{

    override suspend fun getSubCategoriesList(finanzaId: Int?): Flow<Resource<List<ListaSubCategoriasDomain>>> = flow {
        emit(Resource.Loading)
        try {
            val subCategoriesListResponse = subCategoryService.getSubCategoriesList(finanzaId)

            if(subCategoriesListResponse.lista_sub_categorias.isNotEmpty()){
                subCategoryDao.clearSubCategories(
                    finanzaId ?: getFinanceId(userDao)
                )
                subCategoryDao.insertSubCategories(subCategoriesListResponse.toEntity())
            } else {
                emit(Resource.Success(emptyList<ListaSubCategoriasDomain>()))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val gson = Gson()

            val errorResponse = gson.fromJson(errorBody, CommonResponse::class.java)
            val msg = errorResponse.message

            emit(Resource.Error(message = msg))
            return@flow
        } catch(e: Exception) {
            Log.d("SubCategoryRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
            return@flow
        }

        val subCategoriesList = subCategoryDao.getSubCategories(
            finanzaId ?: getFinanceId(userDao)
        ).map { entities ->
            val subCategories = entities.map { it.toDomain() }

            Resource.Success(subCategories)
        }.distinctUntilChanged()

        emitAll(subCategoriesList)
    }.flowOn(Dispatchers.IO)

    override suspend fun getSubCategoryDetails(subCategoryId: Int): Flow<Resource<SubCategoriaDomain>> = flow {
        emit(Resource.Loading)
        try {
            val subCategoryDetails = subCategoryService.getSubCategoryDetails(subCategoryId)

            emit(Resource.Success(subCategoryDetails.toDomain()))
        } catch (e: HttpException) {
            //OJO
            //EN EL FRONT
            //MANEJEN LOS ERRORES,404: NOT FOUND, 500: SERVER ERROR
            val errorBody = e.response()?.errorBody()?.string()
            val gson = Gson()

            val errorResponse = gson.fromJson(errorBody, CommonResponse::class.java)
            val msg = errorResponse.message
            emit(Resource.Error(httpCode = e.code(), message = msg))
        } catch(e: Exception) {
            Log.d("SubCategoryRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getExpensesOptions(): Flow<Resource<List<OptionsDomain>>> = flow {
        emit(Resource.Loading)
        try {
            val expensesTypes = expensesTypesDao.getExpensesTypes().map { entities ->
                val types = entities.map { it.toDomain() }

                types
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val gson = Gson()

            val errorResponse = gson.fromJson(errorBody, CommonResponse::class.java)
            val msg = errorResponse.message

            emit(Resource.Error(message = msg))
        } catch(e: Exception) {
            Log.d("SubCategoryRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun createSubCategory(finanzaId: Int?): Flow<Resource<CommonResponseDomain>> = flow {
        emit(Resource.Loading)
        try {

        } catch (e: HttpException) {
            //ERRORES
            //400: BAD REQUEST, 500: ERROR DEL SERVER
            val errorBody = e.response()?.errorBody()?.string()
            val gson = Gson()

            val errorResponse = gson.fromJson(errorBody, CommonResponse::class.java)
            val msg = errorResponse.message

            emit(Resource.Error(httpCode = e.code(), message = msg))
        } catch(e: Exception) {
            Log.d("SubCategoryRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateSubCategory(subCategoryId: Int): Flow<Resource<CommonResponseDomain>> = flow {
        emit(Resource.Loading)
        try {

        } catch (e: HttpException) {
            //ERRORES
            //400: BAD REQUEST, 404: NOT FOUND, 500: ERROR DEL SERVER
            val errorBody = e.response()?.errorBody()?.string()
            val gson = Gson()

            val errorResponse = gson.fromJson(errorBody, CommonResponse::class.java)
            val msg = errorResponse.message

            emit(Resource.Error(httpCode = e.code(), message = msg))
        } catch(e: Exception) {
            Log.d("SubCategoryRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)
}