package com.tuapp.myapplication.ui.finanzas.finanzaGrupal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tuapp.myapplication.ui.finanzas.FinanzasViewModel

@Composable
fun LeaveFinanceDialog(
    onDismiss: () -> Unit,
    onLeave: () -> Unit,
    finanzasViewModel: FinanzasViewModel
){

    val loadingLeaving by finanzasViewModel.loadingLeave.collectAsStateWithLifecycle()
    val leavingError by finanzasViewModel.leavingError.collectAsStateWithLifecycle()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Abandonar Finanza") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if(loadingLeaving){
                    CircularProgressIndicator()
                } else {
                    Text("Estas seguro que quieres salir de la finanza?")
                    Spacer(modifier = Modifier.height(8.dp))
                    if(leavingError.isNotBlank()){
                        Text(leavingError, fontSize = 15.sp, color = Color.Red)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onLeave()
                }
            ) {
                Text("Abandonar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}