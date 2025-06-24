package com.billetera.appdelclima.router

import android.net.Uri
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Test

import org.junit.Assert.*

class RoutesTest {

    @Test
    fun testShowWeatherRouteWithNormalizedCityName() {
        mockkStatic(Uri::class)
        every { Uri.encode("Calamuchita") } returns "Calamuchita"

        val route = Routes.ShowWeather.createRoute(22.0f, 33.0f, "Calamuchita")

        assertEquals("show_weather/22.0/33.0/Calamuchita", route)
    }

    @Test
    fun testShowWeatherRouteWithSpecialCharacters() {
        mockkStatic(Uri::class)
        every { Uri.encode("Córdoba, Argentina") } returns "C%C3%B3rdoba%2C%20Argentina"

        val route = Routes.ShowWeather.createRoute(22.0f, 33.0f, "Córdoba, Argentina")

        assertEquals("show_weather/22.0/33.0/C%C3%B3rdoba%2C%20Argentina", route)
    }

    @Test
    fun testShowWeatherRouteWithEmptyCityName() {
        mockkStatic(Uri::class)
        every { Uri.encode("") } returns ""

        val route = Routes.ShowWeather.createRoute(22.0f, 33.0f, "")

        assertEquals("show_weather/22.0/33.0/", route)
    }
}