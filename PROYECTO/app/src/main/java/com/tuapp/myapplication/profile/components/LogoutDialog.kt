package com.tuapp.myapplication.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val verde = Color(0xFF2E7D32)
    val verdeClaro = Color(0xFFE6F4EA)

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = verde),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = verde)
            ) {
                Text("Cancelar")
            }
        },
        title = {
            Text(
                "¿Estás Seguro?",
                fontSize = 18.sp,
                color = Color.Black
            )
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}
