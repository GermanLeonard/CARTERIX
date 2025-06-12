package com.tuapp.myapplication.ui.finanzas.finanzaGrupal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun GrupalFinanceScreen(navController: NavController) {
    val currentRoute = Routes.GROUP
    val verde = Color(0xFF2E7D32)

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(verde),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Pantalla de Finanzas Grupales",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 32.dp)
            )
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp, bottom = 60.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp)
                )
                .padding(24.dp)
        ) {

        }

        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        }
    }
}


