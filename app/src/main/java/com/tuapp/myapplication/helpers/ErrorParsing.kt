package com.tuapp.myapplication.helpers

import com.google.gson.Gson
import com.tuapp.myapplication.data.remote.responses.CommonResponse
import retrofit2.HttpException

fun errorParsing(e: HttpException): String {
    val errorBody = e.response()?.errorBody()?.string()
    val gson = Gson()

    val errorResponse = gson.fromJson(errorBody, CommonResponse::class.java)
    return errorResponse.message
}