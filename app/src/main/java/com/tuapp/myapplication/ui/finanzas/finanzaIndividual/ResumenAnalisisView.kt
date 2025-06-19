package com.tuapp.myapplication.ui.finanzas.finanzaIndividual

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResumenAnalisisView(
    ahorroMes: Double,
    ahorroAcumulado: Double,
    metaMensual: Double,
    ingresos: Double,
    egresos: Double,
    diferencia: Double,
    presupuesto: Double,
    consumo: Double,
    variacion: Double
) {

    Column(modifier = Modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text("Ingresos Totales", fontWeight = FontWeight.Bold)
                Text("L. %.2f".format(ingresos), color = Color(0xFF6A1B9A))
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Egresos Totales", fontWeight = FontWeight.Bold)
                Text("L. %.2f".format(egresos), color = Color(0xFF1A237E))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text("Diferencia", fontWeight = FontWeight.Bold)
            Text("L. %.2f".format(diferencia), color = Color(0xFF2E7D32))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Resumen Egresos", fontWeight = FontWeight.Bold)
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.6f), shape = CircleShape)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Presupuesto", fontWeight = FontWeight.Bold)
                Text("L. %.2f".format(presupuesto), color = Color(0xFF6A1B9A))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Consumo", fontWeight = FontWeight.Bold)
                Text("L. %.2f".format(consumo), color = Color(0xFF6A1B9A))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Variación", fontWeight = FontWeight.Bold)
                Text("L. %.2f".format(variacion), color = Color(0xFF2E7D32))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Meta De Ahorro", fontWeight = FontWeight.Bold)

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Ahorro", fontWeight = FontWeight.Medium)
                Text("L. %.2f".format(metaMensual), color = Color(0xFF6A1B9A))

                Spacer(modifier = Modifier.height(4.dp))

                Text("Ahorro Acum.", fontWeight = FontWeight.Medium)
                Text("L. %.2f".format(ahorroAcumulado), color = Color(0xFF6A1B9A))
            }

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 16f
                    drawArc(
                        color = Color.LightGray,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = Color(0xFF2E7D32),
                        startAngle = -90f,
                        sweepAngle = (ahorroMes * 360f / 100).toFloat(),
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${ahorroMes.toInt()}%", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                    Text("Progreso")
                }
            }
        }
    }
}

