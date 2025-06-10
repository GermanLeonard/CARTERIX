package com.tuapp.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.tuapp.myapplication.ui.navigation.AppNavigation
import com.tuapp.myapplication.ui.theme.CarterixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarterixTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}
