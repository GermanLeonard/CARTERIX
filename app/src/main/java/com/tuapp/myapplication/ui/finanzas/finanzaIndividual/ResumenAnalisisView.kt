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
import com.tuapp.myapplication.data.models.financeModels.response.ResumenAhorrosResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.ResumenEgresosResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.ResumenFinancieroResponseDomain

@Composable
fun ResumenAnalisisView(
    resumenFinanciero: ResumenFinancieroResponseDomain,
    resumenEgresos: ResumenEgresosResponseDomain,
    resumenAhorro: ResumenAhorrosResponseDomain
) {

    Column(modifier = Modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text("Ingresos Totales", fontWeight = FontWeight.Bold)
                Text("%.2f".format(resumenFinanciero.ingresos_totales), color = Color(0xFF6A1B9A))
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Egresos Totales", fontWeight = FontWeight.Bold)
                Text("%.2f".format(resumenFinanciero.egresos_totales), color = Color(0xFF1A237E))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text("Diferencia", fontWeight = FontWeight.Bold)
            Text("%.2f".format(resumenFinanciero.diferencia), color = if(resumenFinanciero.diferencia >= 0 ) Color(0xFF2E7D32) else Color(0xFFD25557))
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
                Text("%.2f".format(resumenEgresos.presupuesto_mensual), color = Color(0xFF6A1B9A))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Consumo", fontWeight = FontWeight.Bold)
                Text("%.2f".format(resumenEgresos.consumo_mensual), color = Color(0xFF6A1B9A))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Variación", fontWeight = FontWeight.Bold)
                Text("%.2f".format(resumenEgresos.variacion_mensual), color = if(resumenEgresos.variacion_mensual >= 0 )Color(0xFF2E7D32) else Color(0xFFD25557))
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
                Text("%.2f".format(resumenAhorro.meta), color = Color(0xFF6A1B9A))

                Spacer(modifier = Modifier.height(4.dp))

                Text("Ahorro Acum.", fontWeight = FontWeight.Medium)
                Text("%.2f".format(resumenAhorro.acumulado), color = Color(0xFF6A1B9A))
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
                        sweepAngle = (resumenAhorro.progreso_porcentaje * 360f / 100).toFloat(),
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${resumenAhorro.progreso_porcentaje.toInt()}%", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                    Text("Progreso")
                }
            }
        }
    }
}

