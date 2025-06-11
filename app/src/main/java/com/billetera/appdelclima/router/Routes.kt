package com.billetera.appdelclima.router

import android.net.Uri

sealed class Routes(val route: String) {

    object SelectCity: Routes("select_city")

    data class ShowWeather(
        val locId: String,
        val locLat: Double,
        val locLong: Double,
        val locName: String
    ) : Routes("show_weather/{locId}/{locLat}/{locLong}/{locName}") {
            companion object {
                const val LOCATION_ID = "locId"
                const val LOCATION_LATITUDE = "locLat"
                const val LOCATION_LONGITUDE = "locLong"
                const val LOCATION_NAME = "locName"
                fun create(
                    locId: String,
                    locLat: Double,
                    locLong: Double,
                    locName: String
                ): String {
                    val encodedLocName = Uri.encode(locName)
                    return "show_weather/$locId/$locLat/$locLong/$encodedLocName"
                }
            }
        }

}