package com.tuapp.myapplication.helpers

sealed class Resourse<out T> {
    object Loading: Resourse<Nothing>()
    data class Success<out T>(val data: T): Resourse<T>()
    data class Error(val message: String): Resourse<Nothing>()
}