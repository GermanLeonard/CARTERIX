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
import androidx.compose.ui.draw.clip
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
import com.tuapp.myapplication.ui.components.CustomTopBar
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

    Scaffold(
        topBar = {
            CustomTopBar(Routes.PROFILE, navController)
        },
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = Routes.PROFILE)
        },
        contentColor = Color.Black,
        containerColor = verde
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                        )
                        .padding(horizontal = 25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Spacer(modifier = Modifier.height(100.dp))

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

                    Text(datosUsuario.value.nombre, fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 12.dp))

                    Spacer(modifier = Modifier.height(50.dp))

                    ProfileOption(icon = Icons.Default.Person, text = "Editar Perfil") {
                        navController.navigate(EditProfile)
                    }

                    ProfileOption(icon = Icons.AutoMirrored.Filled.ExitToApp, text = "Cerrar Sesión") {
                        showLogoutDialog = true
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
        }
    }
}

@Composable
fun ProfileOption(
    icon: ImageVector,
    text: String,
    iconBackground: Color = Color(0xFF66BB6A),
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(60.dp) // cuadro más grande
                .clip(RoundedCornerShape(16.dp))
                .background(iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(30.dp) // ícono más grande
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Text(
            text,
            color = Color.Black,
            fontSize = 20.sp,         // texto más grande
            fontWeight = FontWeight.SemiBold
        )
    }
}