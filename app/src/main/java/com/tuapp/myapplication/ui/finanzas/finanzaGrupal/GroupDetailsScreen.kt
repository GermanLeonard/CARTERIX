package com.tuapp.myapplication.ui.finanzas.finanzaGrupal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tuapp.myapplication.ui.finanzas.FinanzasViewModel
import com.tuapp.myapplication.ui.auth.UserViewModel
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.components.CustomTopBar
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun GroupDetailsScreen(
    finanzaId: Int,
    navController: NavHostController,
    viewModel: FinanzasViewModel = viewModel(factory = FinanzasViewModel.Factory),
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
) {

    val verde = Color(0xFF2E7D32)

    val details by viewModel.financeDetails.collectAsStateWithLifecycle()
    val loading by viewModel.loadingDetails.collectAsStateWithLifecycle()
    val detailsError by viewModel.detailsError.collectAsStateWithLifecycle()
    val userCredentials by userViewModel.userCredential.collectAsStateWithLifecycle()

    var showInviteDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteConfirmation by rememberSaveable { mutableStateOf<Int?>(null) }

    var isAdmin by rememberSaveable { mutableStateOf(false) }

    val loadingInvite by viewModel.loadingInvite.collectAsStateWithLifecycle()
    val inviteCode by viewModel.inviteCode.collectAsStateWithLifecycle()
    val inviteError by viewModel.inviteError.collectAsStateWithLifecycle()

    LaunchedEffect(finanzaId) {
        viewModel.getFinanceDetails(finanzaId)
    }

    LaunchedEffect(details) {
        isAdmin = details.finanza_miembros.find {
            it.nombre_usuario == userCredentials.nombre
        }?.rol_usuario == 1
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                Routes.DETALLE_FINANZA,
                navController,
                true
                )
        },
        bottomBar = {
            BottomNavBar(navController, Routes.GROUP)
        },
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = {
                        showInviteDialog = true
                    },
                    containerColor = Color(0xFF2E7D32)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Generar invitación", tint = Color.White)
                }
            }
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
                    .padding(horizontal = 16.dp)
            ) {
                if(loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                } else if(detailsError.isNotBlank()){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Text(detailsError, fontSize = 15.sp, color = Color.Red)
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Descripción del grupo
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Descripción",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF2E7D32)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = details.finanza_descripcion.ifEmpty { "Sin descripción" },
                                    color = Color.DarkGray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    // Header de miembros con contador
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Miembros del grupo",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF2E7D32)
                            )
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = "${details.finanza_miembros.size}",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Lista de miembros
                    items(details.finanza_miembros) { miembro ->
                        MemberCard(
                            miembro = miembro,
                            isCurrentUserAdmin = isAdmin,
                            currentUserName = userCredentials.nombre,
                            onDeleteClick = { showDeleteConfirmation = miembro.id_usuario }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }

    if (showInviteDialog) {
        AlertDialog(
            onDismissRequest = { showInviteDialog = false },
            title = { Text("Código de Invitación") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if(loadingInvite){
                        CircularProgressIndicator()
                    } else if(inviteCode.isBlank()) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                        ) {
                            Text(
                                text = "Generar Codigo",
                                modifier = Modifier.padding(16.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if(inviteError.isNotBlank()){
                            Text(inviteError, fontSize = 15.sp, color = Color.Red)
                        }
                    } else if(inviteCode.isNotBlank()){
                        CopyableCodeField(inviteCode)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showInviteDialog = false }
                ) {
                    Text("Cerrar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.createInvite(finanzaId)
                    }
                ) {
                    Text("Generar")
                }
            }
        )
    }

    showDeleteConfirmation?.let { userId ->
        val miembro = details.finanza_miembros.find { it.id_usuario == userId }
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = null },
            title = { Text("Confirmar eliminación") },
            text = {
                Text("¿Estás seguro de que quieres eliminar a ${miembro?.nombre_usuario} del grupo?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteFromFinance(userId, finanzaId)
                        showDeleteConfirmation = null
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmation = null }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun CopyableCodeField(code: String) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(50.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = code,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black
        )

        IconButton(onClick = {
            clipboardManager.setText(AnnotatedString(code))
            Toast.makeText(context, "Copiado al portapapeles", Toast.LENGTH_SHORT).show()
        }) {
            Icon(Icons.Default.ContentCopy, contentDescription = "Copiar", tint = Color.Black)
        }
    }
}


@Composable
fun MemberCard(
    miembro: com.tuapp.myapplication.data.models.financeModels.response.FinanzaMiembroDomain,
    isCurrentUserAdmin: Boolean,
    currentUserName: String,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (miembro.rol_usuario == 1) Color(0xFFE8F5E8) else Color(0xFFF8F9FA)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (miembro.rol_usuario == 1) Color(0xFF2E7D32) else Color(0xFF6B7280),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (miembro.rol_usuario == 1) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = miembro.nombre_usuario,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                        if (miembro.nombre_usuario == currentUserName) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Tú",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    Text(
                        text = if (miembro.rol_usuario == 1) "Administrador" else "Miembro",
                        color = if (miembro.rol_usuario == 1) Color(0xFF2E7D32) else Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = if (miembro.rol_usuario == 1) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }

            if (isCurrentUserAdmin && miembro.rol_usuario != 1) {
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar miembro",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}