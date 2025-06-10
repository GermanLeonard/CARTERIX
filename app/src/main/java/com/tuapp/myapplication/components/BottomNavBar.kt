package com.tuapp.myapplication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tuapp.myapplication.navigation.Routes

@Composable
fun BottomNavBar(navController: NavController, currentRoute: String) {
    val green = Color(0xFF2E7D32)
    val highlight = Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(green, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            if (currentRoute != Routes.INDIVIDUAL) {
                navController.navigate(Routes.INDIVIDUAL)
            }
        }) {
            Icon(
                Icons.Default.Home,
                contentDescription = "Inicio",
                tint = if (currentRoute == Routes.INDIVIDUAL) highlight else Color.Black
            )
        }

        IconButton(onClick = {
            if (currentRoute != Routes.GROUP) {
                navController.navigate(Routes.GROUP)
            }
        }) {
            Icon(
                Icons.Default.Group,
                contentDescription = "Grupal",
                tint = if (currentRoute == Routes.GROUP) highlight else Color.Black
            )
        }

        IconButton(onClick = {
            if (currentRoute != Routes.PROFILE) {
                navController.navigate(Routes.PROFILE)
            }
        }) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Perfil",
                tint = if (currentRoute == Routes.PROFILE) highlight else Color.Black
            )
        }
    }
}

