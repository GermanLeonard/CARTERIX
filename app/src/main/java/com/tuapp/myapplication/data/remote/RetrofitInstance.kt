package com.tuapp.myapplication.data.remote

import com.tuapp.myapplication.data.remote.categories.CategoriesService
import com.tuapp.myapplication.data.remote.finance.FinanceService
import com.tuapp.myapplication.data.remote.interceptors.AuthInterceptor
import com.tuapp.myapplication.data.remote.subCategories.SubCategoriesService
import com.tuapp.myapplication.data.remote.user.UserService
import com.tuapp.myapplication.data.repository.sensitive.SensitiveInfoRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val BASE_URL = "http://10.0.2.2:8000/"

    fun getInstance(repository: SensitiveInfoRepository): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(AuthInterceptor(repository))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getUserService(repository: SensitiveInfoRepository): UserService{
        return getInstance(repository).create(UserService::class.java)
    }

    fun getFinanceService(repository: SensitiveInfoRepository): FinanceService{
        return getInstance(repository).create(FinanceService::class.java)
    }

    fun getCategoryService(repository: SensitiveInfoRepository): CategoriesService{
        return getInstance(repository).create(CategoriesService::class.java)
    }

    fun getSubcategoryService(repository: SensitiveInfoRepository): SubCategoriesService{
        return getInstance(repository).create(SubCategoriesService::class.java)
    }
}