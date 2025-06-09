package com.billetera.appdelclima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.billetera.appdelclima.ui.ciudad.CiudadScreen
import com.billetera.appdelclima.ui.ciudad.CiudadViewModel
import com.billetera.appdelclima.ui.clima.ClimaScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStore = DataStoreManager(this)
        val viewModel = CiudadViewModel()

        setContent {
            AppDelClimaApp(dataStore, viewModel)
        }
    }
}

@Composable
fun AppDelClimaApp(dataStore: DataStoreManager, viewModel: CiudadViewModel) {
    val navController: NavHostController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    var ciudadInicial by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val guardada = dataStore.obtenerCiudad().first()
        if (!guardada.isNullOrEmpty()) {
            ciudadInicial = guardada
            navController.navigate("clima/$guardada")
        }
    }

    MaterialTheme {
        Surface {
            NavHost(navController = navController, startDestination = "ciudades") {
                composable("ciudades") {
                    CiudadScreen(viewModel) { ciudadSeleccionada ->
                        coroutineScope.launch {
                            dataStore.guardarCiudad(ciudadSeleccionada)
                            navController.navigate("clima/$ciudadSeleccionada")
                        }
                    }
                }

                composable("clima/{ciudad}") { backStackEntry ->
                    val ciudad = backStackEntry.arguments?.getString("ciudad") ?: ""
                    ClimaScreen(ciudad = ciudad, onBack = {
                        navController.popBackStack()
                    })
                }
            }
        }
    }
}