package com.tuapp.myapplication.helpers

sealed class TokenState {
    object Loading : TokenState()
    data class Loaded(val token: String?) : TokenState()
}
