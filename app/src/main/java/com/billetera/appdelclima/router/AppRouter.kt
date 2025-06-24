package com.billetera.appdelclima.router

import androidx.annotation.VisibleForTesting
import androidx.navigation.NavHostController

class AppRouter(private val navController: NavHostController) : Router {
    override fun navigateTo(destination: Routes) {
        val route = getRoute(destination)
        navController.navigate(route)
    }

    override fun navigateBack() {
        navController.popBackStack()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getRoute(destination: Routes): String {
        return when (destination) {
            Routes.SelectCity -> destination.route
            is Routes.ShowWeather -> Routes.ShowWeather.createRoute(
                locLat = destination.locLat,
                locLong = destination.locLong,
                locName = destination.locName
            )
            Routes.Settings -> destination.route
        }
    }

}