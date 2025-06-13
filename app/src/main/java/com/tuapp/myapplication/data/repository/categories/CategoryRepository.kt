package com.tuapp.myapplication.data.repository.categories

import com.tuapp.myapplication.data.models.CommonResponseDomain
import com.tuapp.myapplication.data.models.categoryModels.request.CreateOrUpdateCategorieRequestDomain
import com.tuapp.myapplication.data.models.categoryModels.response.CategorieDataResponseDomain
import com.tuapp.myapplication.data.models.categoryModels.response.CategoriesListDomain
import com.tuapp.myapplication.data.models.categoryModels.response.CategoriesOptionsDomain
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getCategoriesOptions(finanzaId: Int?): Flow<Resource<List<CategoriesOptionsDomain>>>
    suspend fun getCategoriesList(finanzaId: Int?): Flow<Resource<List<CategoriesListDomain>>>
    suspend fun getCategorieData(id: Int, finanzaId: Int?): Flow<Resource<CategorieDataResponseDomain>>
    suspend fun createCategorie(finanzaId: Int?, createOrUpdateCategorieRequest: CreateOrUpdateCategorieRequestDomain): Flow<Resource<CommonResponseDomain>>
    suspend fun updateCategorie(id: Int, createOrUpdateCategorieRequest: CreateOrUpdateCategorieRequestDomain): Flow<Resource<CommonResponseDomain>>
}