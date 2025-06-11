package com.tuapp.myapplication

import android.app.Application
import com.tuapp.myapplication.data.AppProvider

class CarterixApplication: Application() {
    val appProvider by lazy {
        AppProvider(this)
    }
}