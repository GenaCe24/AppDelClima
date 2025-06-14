package com.billetera.appdelclima.router

import androidx.navigation.NavHostController

class AppRouter(private val navController: NavHostController) : Router {
    override fun navigateTo(destination: Routes) {
        val route = getRoute(destination)
        navController.navigate(route)
    }

    override fun navigateBack() {
        navController.popBackStack()
    }

    private fun getRoute(destination: Routes): String {
        val route = when(destination) {
            Routes.SelectCity -> destination.route
            is Routes.ShowWeather ->
                Routes.ShowWeather.createRoute(
                    locLat = destination.locLat,
                    locLong = destination.locLong,
                    locName = destination.locName
                )
        }
        return route
    }
}