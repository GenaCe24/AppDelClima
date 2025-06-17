package com.billetera.appdelclima.ui.clima

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.billetera.appdelclima.DataStoreManager
import com.billetera.appdelclima.presentation.clima.ClimaIntent
import com.billetera.appdelclima.presentation.clima.ClimaViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

data class PronosticoDia(
    val dia: String,
    val tempMax: Int,
    val tempMin: Int,
    val humedad: Int,
    val estado: String
)

@Composable
fun ClimaView(
    ciudadName: String,
    ciudadLat: Float,
    ciudadLong: Float,
    onBack: () -> Unit
) {
    val contexto = LocalContext.current
    val viewModelOwner = LocalViewModelStoreOwner.current

    val climaViewModel = remember(viewModelOwner) {
        ClimaViewModel(
            unidadProvider = {
                val manager = DataStoreManager(contexto)
                runBlocking { manager.obtenerUnidad().first() }
            }
        )
    }

    val state by climaViewModel.state.collectAsState()

    LaunchedEffect(ciudadLat, ciudadLong, ciudadName) {
        climaViewModel.onIntent(ClimaIntent.CargarClima(ciudadLat, ciudadLong, ciudadName))
    }

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
                text = "Clima en $ciudadName",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Text("Cargando clima...", modifier = Modifier.padding(top = 8.dp))
        }

        state.error?.let {
            Text(
                text = "Error: $it",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // --- Clima actual ---
        state.climaActual?.let { clima ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${clima.main.temp.toInt()}°",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = clima.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "N/A",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Máx", fontWeight = FontWeight.Bold)
                            Text("${clima.main.temp_max.toInt()}°")
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Mín", fontWeight = FontWeight.Bold)
                            Text("${clima.main.temp_min.toInt()}°")
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Humedad", fontWeight = FontWeight.Bold)
                            Text("${clima.main.humidity}%")
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Viento", fontWeight = FontWeight.Bold)
                            Text("${clima.wind.speed} m/s")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val texto = buildString {
                    append("Clima actual en $ciudadName: ${state.climaActual?.weather?.firstOrNull()?.description?.replaceFirstChar { it.uppercase() }} ")
                    state.climaActual?.main?.let { main ->
                        append("${main.temp.toInt()}°\n")
                        append("Máx: ${main.temp_max.toInt()}°, Mín: ${main.temp_min.toInt()}°, Humedad: ${main.humidity}%\n\n")
                    }
                    append("Pronóstico para $ciudadName:\n\n")
                    state.pronostico.forEach {
                        append("${it.dia}: ${it.estado}, Máx: ${it.tempMax}°, Mín: ${it.tempMin}°, Humedad: ${it.humedad}%\n")
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
                .padding(bottom = 16.dp),
            enabled = state.climaActual != null && state.pronostico.isNotEmpty()
        ) {
            Text("Compartir pronóstico")
        }

        Text(
            text = "Pronóstico Extendido",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn {
            items(state.pronostico) { dia ->
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
                        Text("Máx: ${dia.tempMax}° | Mín: ${dia.tempMin}°")
                        Text("Humedad: ${dia.humedad}%")
                        Text("Estado: ${dia.estado}")
                    }
                }
            }
        }
    }
}
