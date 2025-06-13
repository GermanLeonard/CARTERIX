package com.tuapp.myapplication.helpers

sealed class Resource<out T> {
    object Loading: Resource<Nothing>()
    data class Success<out T>(val data: T): Resource<T>()
    data class Error(val httpCode: Int? = null, val message: String): Resource<Nothing>()
}