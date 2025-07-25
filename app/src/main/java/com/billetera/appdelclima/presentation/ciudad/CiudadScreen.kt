package com.billetera.appdelclima.ui.ciudad

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billetera.appdelclima.DataStoreManager
import com.billetera.appdelclima.LocationUtils
import com.billetera.appdelclima.MyLocation
import com.billetera.appdelclima.StoredCity
import com.billetera.appdelclima.repository.modelos.Ciudad
import com.billetera.appdelclima.router.Router
import com.billetera.appdelclima.router.Routes
import kotlinx.coroutines.launch

@Composable
fun CiudadScreen(
    viewModel: CiudadViewModel,
    onCiudadSeleccionada: (Ciudad) -> Unit,
    router: Router
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val contexto = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(
            text = "Selecciona una ciudad",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.onIntent(CiudadIntent.BuscarCiudad(it))
            },
            label = { Text("Buscar ciudad") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    val ciudadDetectada = detectarCiudadDesdeUbicacion(contexto) ?: StoredCity(-34.6037f, -58.3816f, "Buenos Aires")
                    viewModel.onIntent(CiudadIntent.SeleccionarCiudad(ciudadDetectada))
                    onCiudadSeleccionada(Ciudad(ciudadDetectada.name, ciudadDetectada.lat, ciudadDetectada.long, "", ""))

                    val favorita = StoredCity(ciudadDetectada.lat, ciudadDetectada.long, ciudadDetectada.name)
                    DataStoreManager(contexto).guardarCiudadFavorita(favorita)
                    Toast.makeText(contexto, "Ciudad guardada como favorita", Toast.LENGTH_SHORT).show()
                }
                /* Reemplazar esta funcion por la anterior si tenes conexion con GPS para detectar ubicacion:
                                        val ciudadDetectada = detectarCiudadDesdeUbicacion(contexto)
                                        if (ciudadDetectada != null) {
                                            Toast.makeText(
                                                contexto,
                                                "Detectado: ${ciudadDetectada.name} (${ciudadDetectada.lat}, ${ciudadDetectada.long})",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            viewModel.onIntent(CiudadIntent.SeleccionarCiudad(ciudadDetectada))
                                            onCiudadSeleccionada(Ciudad(ciudadDetectada.name, ciudadDetectada.lat, ciudadDetectada.long, "", ""))
                                        } else {
                                            Toast.makeText(contexto, "No se pudo obtener la ubicación", Toast.LENGTH_LONG).show()
                                        }
                    */

            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C4DFF))
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Detectar mi ubicación")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.buscando) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Text("Buscando ciudades...", modifier = Modifier.padding(top = 8.dp))
        }

        state.error?.let {
            Text(
                text = "Error: $it",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.ciudades) { ciudad ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.onIntent(CiudadIntent.SeleccionarCiudad(StoredCity(ciudad.lat, ciudad.lon, ciudad.name)))
                        onCiudadSeleccionada(ciudad)

                        coroutineScope.launch {
                            val favorita = StoredCity(ciudad.lat, ciudad.lon, ciudad.name)
                            DataStoreManager(contexto).guardarCiudadFavorita(favorita)
                            Toast.makeText(contexto, "Ciudad guardada como favorita", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .padding(12.dp)) {
                    Text(text = ciudad.name, fontSize = 16.sp)
                    ciudad.state?.let { stateName ->
                        if (stateName.isNotBlank()) {
                            Text(text = stateName, fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                    Text(text = ciudad.country, fontSize = 14.sp, color = Color.Gray)
                    Divider(modifier = Modifier.padding(top = 8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { router.navigateTo(Routes.Settings) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ir a Configuración")
        }
    }
}

suspend fun detectarCiudadDesdeUbicacion(context: Context): StoredCity? {
    val location = MyLocation.getLastKnownLocation(context)
    return location?.let {
        LocationUtils.obtenerCiudadDesdeUbicacion(context)
    }
}

