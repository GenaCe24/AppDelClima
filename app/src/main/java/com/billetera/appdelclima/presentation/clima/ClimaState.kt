package com.billetera.appdelclima.presentation.clima

import com.billetera.appdelclima.repository.modelos.Clima // Importa tu modelo Clima

data class ClimaState(
    val climaActual: Clima? = null,
    val pronostico: List<PronosticoDiaUI> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Un modelo simplificado para mostrar cada día del pronóstico en la interfaz de usuario
data class PronosticoDiaUI(
    val dia: String,
    val tempMax: Int,
    val tempMin: Int,
    val humedad: Int,
    val estado: String,
    val iconUrl: String? = null
)

