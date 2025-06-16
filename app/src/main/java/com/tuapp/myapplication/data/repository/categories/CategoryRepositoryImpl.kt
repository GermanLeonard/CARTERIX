package com.tuapp.myapplication.data.repository.categories

import android.util.Log
import com.tuapp.myapplication.data.database.dao.category.CategoriaEgresoDao
import com.tuapp.myapplication.data.database.dao.user.UserDao
import com.tuapp.myapplication.data.database.entities.category.toDomain
import com.tuapp.myapplication.data.models.CommonResponseDomain
import com.tuapp.myapplication.data.models.categoryModels.request.CreateOrUpdateCategorieRequestDomain
import com.tuapp.myapplication.data.models.categoryModels.request.toRequest
import com.tuapp.myapplication.data.models.categoryModels.response.CategorieDataResponseDomain
import com.tuapp.myapplication.data.models.categoryModels.response.CategoriesListDomain
import com.tuapp.myapplication.data.models.categoryModels.response.CategoriesOptionsDomain
import com.tuapp.myapplication.data.remote.categories.CategoriesService
import com.tuapp.myapplication.data.remote.responses.categorieResponse.toDomain
import com.tuapp.myapplication.data.remote.responses.categorieResponse.toEntity
import com.tuapp.myapplication.data.remote.responses.toDomain
import com.tuapp.myapplication.helpers.Resource
import com.tuapp.myapplication.helpers.errorParsing
import com.tuapp.myapplication.helpers.getFinanceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class CategoryRepositoryImpl(
    private val categoriesService: CategoriesService,
    private val categoriaEgresoDao: CategoriaEgresoDao,
    private val userDao: UserDao,
): CategoryRepository {

    override suspend fun getCategoriesOptions(finanzaId: Int?): Flow<Resource<List<CategoriesOptionsDomain>>> = flow {
        emit(Resource.Loading)
        try {
            val categorieOptions = categoriesService.getCategoriesOptions(finanzaId)

            emit(Resource.Success(categorieOptions.toDomain()))
        } catch (e: HttpException) {
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        } catch(e: Exception) {
            Log.d("CategorieRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getCategoriesList(finanzaId: Int?): Flow<Resource<List<CategoriesListDomain>>> = flow {
        emit(Resource.Loading)
        try {
            val categoriesList = categoriesService.getCategoriesList(finanzaId)

            if(categoriesList.lista_categorias.isNotEmpty()){
                categoriaEgresoDao.clearCategories(
                    finanzaId ?: getFinanceId(userDao)
                )
                categoriaEgresoDao.insertCategories(categoriesList.toEntity())
            }else{
                emit(Resource.Success(emptyList()))
                return@flow
            }
        } catch (e: HttpException) {
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
            return@flow
        } catch(e: Exception) {
            Log.d("CategorieRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
            return@flow
        }

        val cachedCategoriesList = categoriaEgresoDao.getCategories(
            finanzaId ?: getFinanceId(userDao)
        ).map { entities ->
            val categories = entities.map { it.toDomain() }
                Resource.Success(categories)
        }.distinctUntilChanged()

        emitAll(cachedCategoriesList)
    }.flowOn(Dispatchers.IO)

    override suspend fun getCategorieData(
        id: Int,
        finanzaId: Int?
    ): Flow<Resource<CategorieDataResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val categorieData = categoriesService.getCategorieData(id, finanzaId)

            emit(Resource.Success(categorieData.toDomain()))
        } catch (e: HttpException) {
            //OJO
            //EN EL FRONT
            //MANEJEN LOS ERRORES, CODIGO 400: BAD REQUEST, 404: NOT FOUND, 500: SERVER ERROR
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        } catch(e: Exception) {
            Log.d("CategorieRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun createCategorie(
        finanzaId: Int?,
        createOrUpdateCategorieRequest: CreateOrUpdateCategorieRequestDomain
    ): Flow<Resource<CommonResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val createCategorieResponse = categoriesService.createCategorie(
                finanzaId,
                createOrUpdateCategorieRequest.toRequest()
            )
            if(createCategorieResponse.success){
                emit(Resource.Success(createCategorieResponse.toDomain()))
            }
        }catch (e: HttpException) {
            //OJO
            //EN EL FRONT
            //MANEJEN LOS ERRORES, CODIGO 400: BAD REQUEST 500: SERVER ERROR
            val msg = errorParsing(e)

            emit(Resource.Error(message = msg))
        }catch(e: Exception) {
            Log.d("CategorieRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateCategorie(
        id: Int,
        createOrUpdateCategorieRequest: CreateOrUpdateCategorieRequestDomain
    ): Flow<Resource<CommonResponseDomain>> = flow {
        emit(Resource.Loading)
        try {
            val updateCategorieResponse = categoriesService.updateCategorie(
                id,
                createOrUpdateCategorieRequest.toRequest()
            )

            if(updateCategorieResponse.success){
                emit(Resource.Success(updateCategorieResponse.toDomain()))
            }
        }catch (e: HttpException) {
            //OJO
            //EN EL FRONT
            //MANEJEN LOS ERRORES, CODIGO 400: BAD REQUEST, 404: NOT FOUND, 500: SERVER ERROR
            val msg = errorParsing(e)

            emit(Resource.Error(httpCode = e.code(), message = msg))
        }catch(e: Exception) {
            Log.d("CategorieRepository", "Error al hacer la petición: ${e.message}")
            emit(Resource.Error(message = "Error inesperado: ${e.localizedMessage ?: "Desconocido"}"))
        }
    }.flowOn(Dispatchers.IO)
}
