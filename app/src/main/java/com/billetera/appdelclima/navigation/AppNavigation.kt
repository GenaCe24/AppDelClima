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
import com.billetera.appdelclima.StoredCity
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
    var ciudadInicial by remember { mutableStateOf<StoredCity?>(null) }
    LaunchedEffect(Unit) {
        val guardada = dataStore.obtenerCiudad().first()
        println("Obteniendo ciudad guardada: ${guardada.toString()})")

        if (guardada != null) {
            ciudadInicial = guardada
            navController.navigate(
                Routes.ShowWeather.createRoute(guardada.lat, guardada.long, guardada.name)
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
                        dataStore.guardarCiudad(
                            ciudadSeleccionada.lat,
                            ciudadSeleccionada.lon,
                            ciudadSeleccionada.name )

                        router.navigateTo(Routes.ShowWeather(
                            ciudadSeleccionada.lat,
                            ciudadSeleccionada.lon,
                            ciudadSeleccionada.name))

                }
            }
        }

        composable(
            route = Routes.ShowWeather.ROUTE_PATTERN,
            arguments = listOf(
                navArgument(Routes.ShowWeather.LOCATION_LATITUDE) { type = NavType.FloatType },
                navArgument(Routes.ShowWeather.LOCATION_LONGITUDE) { type = NavType.FloatType },
                navArgument(Routes.ShowWeather.LOCATION_NAME) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val lat  = args.getFloat(Routes.ShowWeather.LOCATION_LATITUDE)
            val long  = args.getFloat(Routes.ShowWeather.LOCATION_LONGITUDE)
            val name = args.getString(Routes.ShowWeather.LOCATION_NAME)!!

            // TODO: acá hay que actualizar la vista para que reciba los tres argumentos
            ClimaView(
                ciudadName = name,
                ciudadLat = lat,
                ciudadLong = long,
                onBack = { router.navigateBack() }
            )

        }
    }
}