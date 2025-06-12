package com.billetera.appdelclima.router

import androidx.navigation.NavHostController

class AppRouter(private val navController: NavHostController) {
    fun navigateTo(destination: Routes) {
        val route = getRoute(destination)
        navController.navigate(route)
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    private fun getRoute(destination: Routes): String {
        val route = when(destination) {
            Routes.SelectCity -> destination.route
            is Routes.ShowWeather ->
                Routes.ShowWeather.createRoute(
                    locId = destination.locId,
                    locLat = destination.locLat,
                    locLong = destination.locLong,
                    locName = destination.locName
                )
        }
        return route
    }
}