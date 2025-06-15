package com.billetera.appdelclima.presentation.clima

sealed class ClimaIntent {
    data class CargarClima(val lat: Float, val lon: Float, val cityName: String) : ClimaIntent()
}