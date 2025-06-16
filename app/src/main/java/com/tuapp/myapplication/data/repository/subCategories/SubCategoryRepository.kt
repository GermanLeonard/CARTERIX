package com.tuapp.myapplication.data.repository.subCategories

import com.tuapp.myapplication.data.models.CommonResponseDomain
import com.tuapp.myapplication.data.models.categoryModels.request.CreateOrUpdateCategorieRequestDomain
import com.tuapp.myapplication.data.models.subCategoryModels.request.CreateOrUpdateSubCategoryDomain
import com.tuapp.myapplication.data.models.subCategoryModels.response.ListaSubCategoriasDomain
import com.tuapp.myapplication.data.models.subCategoryModels.response.OptionsDomain
import com.tuapp.myapplication.data.models.subCategoryModels.response.SubCategoriaDomain
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.Flow

interface SubCategoryRepository {
    suspend fun getSubCategoriesList(finanzaId: Int?): Flow<Resource<List<ListaSubCategoriasDomain>>>
    suspend fun getSubCategoryDetails(subCategoryId: Int): Flow<Resource<SubCategoriaDomain>>
    suspend fun getExpensesOptions(): Flow<Resource<List<OptionsDomain>>>
    suspend fun createSubCategory(finanzaId: Int?, createOrUpdateCategory: CreateOrUpdateSubCategoryDomain): Flow<Resource<CommonResponseDomain>>
    suspend fun updateSubCategory(subCategoryId: Int, createOrUpdateCategory: CreateOrUpdateSubCategoryDomain): Flow<Resource<CommonResponseDomain>>
}