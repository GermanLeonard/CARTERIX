package com.tuapp.myapplication.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tuapp.myapplication.components.BottomNavBar
import com.tuapp.myapplication.navigation.Routes

@Composable
fun ProfileScreen(navController: NavController) {
    val currentRoute = Routes.PROFILE

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
                .padding(24.dp)
        ) {
            Text(
                text = "Pantalla de Perfil de Usuario",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        }
    }
}



