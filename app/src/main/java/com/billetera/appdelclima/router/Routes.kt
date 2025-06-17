package com.billetera.appdelclima.router

import android.net.Uri

sealed class Routes(val route: String) {

    object SelectCity: Routes("select_city")
    object Settings : Routes("settings")

    data class ShowWeather(
        val locLat: Float,
        val locLong: Float,
        val locName: String
    ) : Routes("show_weather/{locLat}/{locLong}/{locName}") {
            companion object {
                const val LOCATION_LATITUDE = "locLat"
                const val LOCATION_LONGITUDE = "locLong"
                const val LOCATION_NAME = "locName"
                const val ROUTE_PATTERN = "show_weather/{locLat}/{locLong}/{locName}"
                fun createRoute(
                    locLat: Float,
                    locLong: Float,
                    locName: String
                ): String {
                    val encodedLocName = Uri.encode(locName)
                    return "show_weather/$locLat/$locLong/$encodedLocName"
                }
            }
        }

}