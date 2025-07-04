package com.tuapp.myapplication.ui.profile

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.auth.UserViewModel
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.components.CustomTopBar
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
        if(nombre.isBlank()) nombre = userCredentials?.nombre ?: ""
        if(email.isBlank()) email = userCredentials?.correo ?: ""
    }

    val loadingChangeProfile by userViewModel.loadingChangeProfile.collectAsStateWithLifecycle()
    val changeProfileError by userViewModel.changeProfileError.collectAsStateWithLifecycle()

    val loadingChangePassword by userViewModel.loadingChangePassword.collectAsStateWithLifecycle()
    val changePasswordError by userViewModel.changePasswordError.collectAsStateWithLifecycle()

    // VALIDACIONES
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var nuevaContrasenaError by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CustomTopBar(Routes.EDIT_PROFILE, navController, true)
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if(loadingChangeProfile || loadingChangePassword){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                    Text("Información de perfil", fontWeight = FontWeight.Bold, fontSize = 25.sp)

                    CustomField("Nombre completo", nombre) { nombre = it }

                    CustomField("Email", email) {
                        email = it
                        emailError = null
                    }

                    // MOSTRAR ERROR DE EMAIL
                    if (changeProfileError.isBlank()) {
                        Text(emailError ?: "", color = Color.Red, fontSize = 12.sp)
                    } else {
                        Text(changeProfileError, color = Color.Red, fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            // VALIDACION de email
                            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                emailError = "Correo inválido"
                            } else {
                                emailError = null
                                userViewModel.changeProfile(nombre, email)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = verde),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        Text("Guardar")
                    }

                Spacer(modifier = Modifier.height(30.dp))

                    Text("Actualizar contraseña", fontWeight = FontWeight.Bold, fontSize = 25.sp)

                    CustomField("Contraseña actual", contrasenaActual) { contrasenaActual = it }

                    CustomField("Nueva contraseña", nuevaContrasena) {
                        nuevaContrasena = it
                        nuevaContrasenaError = null
                    }


                    CustomField("Confirmar contraseña", confirmarContrasena) {
                        confirmarContrasena = it
                    }

                if (changePasswordError.isBlank()) {
                    Text(nuevaContrasenaError ?: "", color = Color.Red, fontSize = 12.sp)
                } else{
                    Text(changePasswordError, color = Color.Red, fontSize = 12.sp)
                }

                    Button(
                        onClick = {
                            val regexContrasena = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[A-Za-z\\d\\W_]{8,}$")
                            var valido = true

                            if (!regexContrasena.matches(nuevaContrasena)) {
                                nuevaContrasenaError = "La contraseña ebe tener al menos 8 caracteres, mayúscula, minúscula, número y símbolo"
                                valido = false
                            }

                            if (valido) {
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
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = if(placeholder == "Email") KeyboardType.Email else KeyboardType.Text)
    )
}

