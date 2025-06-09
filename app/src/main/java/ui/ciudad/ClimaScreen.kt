package com.billetera.appdelclima.ui.clima

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class PronosticoDia(
    val dia: String,
    val tempMax: Int,
    val tempMin: Int,
    val humedad: Int,
    val estado: String
)

@Composable
fun ClimaScreen(ciudad: String, onBack: () -> Unit) {
    val contexto = LocalContext.current

    val pronostico = listOf(
        PronosticoDia("Lunes", 26, 15, 70, "Soleado ☀️"),
        PronosticoDia("Martes", 24, 13, 65, "Parcialmente nublado ⛅"),
        PronosticoDia("Miércoles", 22, 12, 80, "Lluvia 🌧️"),
        PronosticoDia("Jueves", 27, 17, 60, "Soleado ☀️"),
        PronosticoDia("Viernes", 25, 14, 75, "Tormenta ⛈️"),
        PronosticoDia("Sábado", 28, 18, 68, "Nublado ☁️"),
        PronosticoDia("Domingo", 29, 19, 58, "Soleado ☀️")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F7))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Clima en $ciudad",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val texto = buildString {
                    append("Pronóstico para $ciudad:\n\n")
                    pronostico.forEach {
                        append("${it.dia}: ${it.estado}, Máx: ${it.tempMax}°C, Mín: ${it.tempMin}°C, Humedad: ${it.humedad}%\n")
                    }
                }

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, texto)
                }
                val chooser = Intent.createChooser(intent, "Compartir pronóstico")
                contexto.startActivity(chooser)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Compartir pronóstico")
        }

        LazyColumn {
            items(pronostico) { dia ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = dia.dia, fontWeight = FontWeight.Bold)
                        Text("Máx: ${dia.tempMax}°C | Mín: ${dia.tempMin}°C")
                        Text("Humedad: ${dia.humedad}%")
                        Text("Estado: ${dia.estado}")
                    }
                }
            }
        }
    }
}