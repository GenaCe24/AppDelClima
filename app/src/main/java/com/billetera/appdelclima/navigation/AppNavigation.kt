package com.billetera.appdelclima.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.billetera.appdelclima.DataStoreManager
import com.billetera.appdelclima.router.AppRouter
import com.billetera.appdelclima.router.Routes
import com.billetera.appdelclima.ui.ciudad.CiudadScreen
import com.billetera.appdelclima.ui.ciudad.CiudadViewModel
import com.billetera.appdelclima.ui.clima.ClimaView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: Routes = Routes.SelectCity,
    coroutineScope: CoroutineScope,
    ciudadViewModel: CiudadViewModel,
    dataStore: DataStoreManager
) {
    val router = AppRouter(navController)
    var ciudadInicial by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        val guardada = dataStore.obtenerCiudad().first()
        if (!guardada.isNullOrEmpty()) {
            ciudadInicial = guardada
            navController.navigate(
                Routes.ShowWeather.createRoute(guardada, 0.0f, 0.0f, guardada)
            )
        }
    }
    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(Routes.SelectCity.route) {
            // TODO: instanciar el ViewModel dentro del package "ciudad", volarlo de Main
            CiudadScreen(ciudadViewModel) { ciudadSeleccionada ->
                coroutineScope.launch {
                    // TODO: acá hay que actualizar 'guardarCiudad' para guardar toda la data de la ciudad, que inclue ID, LATITUD, LONGITUD, y NOMBRE
                    dataStore.guardarCiudad(ciudadSeleccionada)
                    // TODO: acá hay que pasar la data posta (ID, LATITUD, LONGITUD, y NOMBRE) y la vista tiene que leer todo, actualmente en la línea 61 solo se está leyendo el cuarto argumento "name"
                    router.navigateTo(Routes.ShowWeather(ciudadSeleccionada,
                        0f, 0f, ciudadSeleccionada))
                }
            }
        }

        composable(
            route = Routes.ShowWeather.ROUTE_PATTERN,
            arguments = listOf(
                navArgument(Routes.ShowWeather.LOCATION_ID) { type = NavType.StringType },
                navArgument(Routes.ShowWeather.LOCATION_LATITUDE) { type = NavType.FloatType },
                navArgument(Routes.ShowWeather.LOCATION_LONGITUDE) { type = NavType.FloatType },
                navArgument(Routes.ShowWeather.LOCATION_NAME) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val id   = args.getString(Routes.ShowWeather.LOCATION_ID)!!
            val lat  = args.getFloat(Routes.ShowWeather.LOCATION_LATITUDE)
            val long  = args.getFloat(Routes.ShowWeather.LOCATION_LONGITUDE)
            val name = args.getString(Routes.ShowWeather.LOCATION_NAME)!!

            // TODO: acá hay que actualizar la vista para que reciba los cuatro argumentos
            ClimaView(
                ciudad = name,
                onBack = { router.navigateBack() }
            )

        }
    }
}