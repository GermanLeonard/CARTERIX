package com.tuapp.myapplication.profile

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.auth.UserViewModel
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun EditProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
) {
    val verde = Color(0xFF2E7D32)

    val userCredentials by userViewModel.userCredential.collectAsStateWithLifecycle()

    var nombre by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var contrasenaActual by rememberSaveable { mutableStateOf("") }
    var nuevaContrasena by rememberSaveable { mutableStateOf("") }
    var confirmarContrasena by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(userCredentials) {
        if(nombre.isBlank()) nombre = userCredentials.nombre
        if(email.isBlank()) email = userCredentials.correo
    }

    // VALIDACIONES
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var nuevaContrasenaError by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmarContrasenaError by rememberSaveable { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(verde),
                contentAlignment = Alignment.TopCenter
            ) {
                Text("Editar perfil", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(top = 32.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Información de perfil", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                CustomField("Nombre completo", nombre) { nombre = it }

                CustomField("Email", email) {
                    email = it
                    emailError = null
                }

                // MOSTRAR ERROR DE EMAIL
                if (emailError != null) {
                    Text(emailError!!, color = Color.Red, fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        // VALIDACION de email
                        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            emailError = "Correo inválido"
                        } else {
                            emailError = null
                            // Aqui iria la logica real de actualizacion de datos
                            //manejen errores y success porfavor
                            userViewModel.changeProfile(nombre, email)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = verde),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Text("Guardar")
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("Actualizar contraseña", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                CustomField("Contraseña actual", contrasenaActual) { contrasenaActual = it }

                CustomField("Nueva contraseña", nuevaContrasena) {
                    nuevaContrasena = it
                    nuevaContrasenaError = null
                }

                // MOSTRAR ERROR DE NUEVA CONTRASEÑA
                if (nuevaContrasenaError != null) {
                    Text(nuevaContrasenaError!!, color = Color.Red, fontSize = 12.sp)
                }

                CustomField("Confirmar contraseña", confirmarContrasena) {
                    confirmarContrasena = it
                    confirmarContrasenaError = null
                }

                // MOSTRAR ERROR DE CONFIRMACION
                if (confirmarContrasenaError != null) {
                    Text(confirmarContrasenaError!!, color = Color.Red, fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        val regexContrasena = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d\\W]).{8,}$")
                        var valido = true

                        if (!regexContrasena.matches(nuevaContrasena)) {
                            nuevaContrasenaError = "Debe tener al menos 8 caracteres, mayúscula, minúscula y número o símbolo"
                            valido = false
                        }

                        if (nuevaContrasena != confirmarContrasena) {
                            confirmarContrasenaError = "Las contraseñas no coinciden"
                            valido = false
                        }

                        if (valido) {
                            // Aqui iria la logica real de actualizacióon de contraseña
                            userViewModel.changePassword(contrasenaActual, nuevaContrasena, confirmarContrasena)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = verde),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Text("Guardar")
                }
            }
        }

        BottomNavBar(navController = navController, currentRoute = Routes.PROFILE)
    }
}

@Composable
fun CustomField(placeholder: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(25.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFE6F4EA),
            unfocusedContainerColor = Color(0xFFE6F4EA),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

