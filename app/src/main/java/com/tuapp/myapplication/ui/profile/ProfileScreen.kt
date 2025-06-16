package com.tuapp.myapplication.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tuapp.myapplication.components.BottomNavBar
import com.tuapp.myapplication.navigation.Routes
import com.tuapp.myapplication.profile.components.LogoutDialog

@Composable
fun ProfileScreen(navController: NavController) {
    val verde = Color(0xFF2E7D32)
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Reemplazar esta línea en backend

    val nombreUsuario by remember { mutableStateOf("Cris RKT") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(verde),
                contentAlignment = Alignment.TopCenter
            ) {
                Text("Perfil", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(top = 32.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp))
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFE6F4EA),
                    modifier = Modifier
                        .size(80.dp)
                        .offset(y = (-40).dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(nombreUsuario.first().toString(), fontSize = 30.sp, fontWeight = FontWeight.Bold, color = verde)
                    }
                }

                Text(nombreUsuario, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 12.dp))

                Spacer(modifier = Modifier.height(8.dp))

                ProfileOption(icon = Icons.Default.Person, text = "Editar Perfil") {
                    navController.navigate(Routes.EDIT_PROFILE)
                }

                ProfileOption(icon = Icons.Default.ExitToApp, text = "Cerrar Sesión", color = Color.Red) {
                    showLogoutDialog = true
                }
            }
        }

        if (showLogoutDialog) {
            LogoutDialog(
                onConfirm = {
                    showLogoutDialog = false
                    navController.navigate(Routes.LOGIN) { popUpTo(0) }
                },
                onDismiss = { showLogoutDialog = false }
            )
        }

        BottomNavBar(navController = navController, currentRoute = Routes.PROFILE)
    }
}

@Composable
fun ProfileOption(icon: ImageVector, text: String, color: Color = Color.Black, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, color = color, fontSize = 16.sp)
    }
}
