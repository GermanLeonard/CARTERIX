package com.tuapp.myapplication.data.remote.interceptors

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        if (path.contains("usuario/login") || path.contains("usuario/register")) {
            return chain.proceed(request)
        }

        val token = tokenProvider()

        val newRequest = request.newBuilder().apply {
            Log.d("AuthInterceptor", "Token: $token")
            token?.let {
                addHeader("Authorization", "Bearer $it")
            }
        }.build()

        return chain.proceed(newRequest)
    }
}