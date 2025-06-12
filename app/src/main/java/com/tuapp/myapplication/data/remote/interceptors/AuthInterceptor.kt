package com.tuapp.myapplication.data.remote.interceptors

import android.util.Log
import com.tuapp.myapplication.data.repository.sensitive.SensitiveInfoRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val repository: SensitiveInfoRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        if (path.contains("usuario/login") || path.contains("usuario/register")) {
            return chain.proceed(request)
        }

        val token = runBlocking {
            repository.authenticationToken.first() ?: ""
        }

        val newRequest = request.newBuilder().apply {
            Log.d("AuthInterceptor", "Token: $token")
            token?.let {
                addHeader("Authorization", "Bearer $it")
            }
        }.build()

        return chain.proceed(newRequest)
    }
}