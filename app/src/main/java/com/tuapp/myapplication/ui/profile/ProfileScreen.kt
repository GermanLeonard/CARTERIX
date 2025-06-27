package com.tuapp.myapplication.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.profile.components.LogoutDialog
import com.tuapp.myapplication.ui.auth.UserViewModel
import com.tuapp.myapplication.ui.navigation.EditProfile
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
) {
    val verde = Color(0xFF2E7D32)
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }

    val datosUsuario = userViewModel.userCredential.collectAsStateWithLifecycle()

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
                        val letraInicial = datosUsuario.value.nombre.firstOrNull()?.toString() ?: "?"

                        Text(letraInicial, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = verde)
                    }
                }

                Text(datosUsuario.value.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 12.dp))

                Spacer(modifier = Modifier.height(8.dp))

                ProfileOption(icon = Icons.Default.Person, text = "Editar Perfil") {
                    navController.navigate(EditProfile)
                }

                ProfileOption(icon = Icons.AutoMirrored.Filled.ExitToApp, text = "Cerrar Sesión", color = Color.Red) {
                    showLogoutDialog = true
                }
            }
        }

        if (showLogoutDialog) {
            LogoutDialog(
                onConfirm = {
                    userViewModel.closeSession()
                    showLogoutDialog = false
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
