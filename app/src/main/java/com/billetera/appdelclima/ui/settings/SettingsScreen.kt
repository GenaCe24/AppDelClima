package com.billetera.appdelclima.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.billetera.appdelclima.DataStoreManager
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    dataStore: DataStoreManager
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
    }
}

