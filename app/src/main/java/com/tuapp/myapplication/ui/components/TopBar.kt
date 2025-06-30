package com.tuapp.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.navigation.GroupDetailsScreen

@Composable
fun CustomTopBar(
    title: String,
    navController: NavController,
    showBackArrow: Boolean = false,
    showOptions: Boolean = false,
    finanzaId: Int? = null
) {
    val verde = Color(0xFF2E7D32)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(verde)
    ) {
        if (showBackArrow) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )

        if (showOptions && finanzaId != null) {
            IconButton(
                onClick = {
                    navController.navigate(GroupDetailsScreen(finanzaId))
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
            ) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Detalles", tint = Color.White)
            }
        }
    }
}
