package com.tuapp.myapplication.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tuapp.myapplication.R
import com.tuapp.myapplication.ui.navigation.LoginScreen

@Composable
fun RegisterScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }

    // Mensajes de error
    var correoError by rememberSaveable { mutableStateOf<String?>(null) }
    var contrasenaError by rememberSaveable { mutableStateOf<String?>(null) }

    val registerLoading by userViewModel.registerLoading.collectAsStateWithLifecycle()
    val emailError by userViewModel.registerEmailError.collectAsStateWithLifecycle()
    val apiError by userViewModel.registerApiError.collectAsStateWithLifecycle()

    val registerMessage by userViewModel.registerMessage.collectAsStateWithLifecycle()

    val verde = Color(0xFF2E7D32)
    val verdeClaro = Color(0xFF66BB6A)

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo verde superior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(verde)
        )

        // Contenedor blanco con borde redondeado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp)
                )
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.carterix),
                    contentDescription = "Logo",
                    tint = verde,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Carterix",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = verde
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Crea tu cuenta",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Bienvenido al camino de las finanzas responsables",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(9.dp))

            // Campo Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                placeholder = { Text("Nombre completo") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = verde,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = verde
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Campo Correo
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    correoError = null
                },
                placeholder = { Text("Correo electronico") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray)
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                isError = correoError != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = verde,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = verde,
                    errorBorderColor = Color.Red
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            if(emailError.isBlank() && apiError.isBlank()){
                Text(correoError ?: "", color = Color.Red, fontSize = 12.sp)
            } else if(apiError.isNotBlank()) {
                Text(apiError, color = Color.Red, fontSize = 12.sp)
            } else {
                Text(emailError, color = Color.Red, fontSize = 12.sp)
            }

            // Campo Contraseña
            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    contrasenaError = null
                },
                placeholder = { Text("Contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray)
                },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                isError = contrasenaError != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = verde,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = verde,
                    errorBorderColor = Color.Red
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
            )

            Text(contrasenaError ?: "", color = Color.Red, fontSize = 12.sp)
            if(registerMessage.isNotBlank()){
                Text(registerMessage, color = verde, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val correoRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
                    val contrasenaRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])[A-Za-z\\d\\W_]{8,}$")

                    var valido = true

                    if (!correo.matches(correoRegex)) {
                        correoError = "Correo invalido"
                        valido = false
                    }
                    if (!contrasena.matches(contrasenaRegex)) {
                        contrasenaError =
                            "Contraseña debe tener al menos 8 caracteres, mayuscula, minuscula y número o simbolo"
                        valido = false
                    }

                    if (valido) {
                        userViewModel.registerUser(nombre, correo, contrasena)
                    }
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = verde)
            ) {
                Text("Registrarse", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("¿Ya tienes cuenta?", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = {
                    navController.navigate(LoginScreen)
                }) {
                    Text("Iniciar sesión", color = verdeClaro, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (registerLoading) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.White),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

