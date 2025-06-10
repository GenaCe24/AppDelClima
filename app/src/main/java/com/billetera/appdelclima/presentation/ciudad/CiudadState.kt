package com.billetera.appdelclima.ui.ciudad

data class CiudadState(
    val ciudades: List<String> = emptyList(),
    val ciudadSeleccionada: String? = null,
    val buscando: Boolean = false,
    val error: String? = null
)