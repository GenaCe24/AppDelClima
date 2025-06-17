package com.billetera.appdelclima.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billetera.appdelclima.DataStoreManager
import com.billetera.appdelclima.router.Router
import com.billetera.appdelclima.router.Routes
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    dataStore: DataStoreManager,
    router: Router
) {
    val scope = rememberCoroutineScope()
    val unidadSeleccionada by dataStore.obtenerUnidad().collectAsState(initial = "metric")

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(
            text = "Configuración",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(onClick = onBack) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Unidad de temperatura:")
        Row(modifier = Modifier.padding(top = 8.dp)) {
            RadioButton(
                selected = unidadSeleccionada == "metric",
                onClick = {
                    scope.launch { dataStore.guardarUnidad("metric") }
                }
            )
            Text("Celsius (°C)", modifier = Modifier.padding(end = 16.dp))

            RadioButton(
                selected = unidadSeleccionada == "imperial",
                onClick = {
                    scope.launch { dataStore.guardarUnidad("imperial") }
                }
            )
            Text("Fahrenheit (°F)")
        }

        val historial by dataStore.obtenerHistorial().collectAsState(initial = emptyList())

        Spacer(modifier = Modifier.height(24.dp))
        Text("Historial de ciudades:", fontWeight = FontWeight.Bold)

        historial.forEach { ciudad ->
            Button(
                onClick = {
                    scope.launch {
                        dataStore.guardarCiudad(ciudad.lat, ciudad.long, ciudad.name)
                        router.navigateTo(Routes.ShowWeather(ciudad.lat, ciudad.long, ciudad.name))
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text(ciudad.name)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    dataStore.borrarHistorial()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Borrar historial", color = Color.White)
        }


    }
}

