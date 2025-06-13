package com.billetera.appdelclima.ui.ciudad

import com.billetera.appdelclima.repository.modelos.Ciudad

data class CiudadState(
    //TODO Lista de nombres de ciudades (podría ser Ciudad en el futuro)
    val ciudades: List<String> = emptyList(),
    val ciudadSeleccionada: String? = null,
    val buscando: Boolean = false,
    val error: String? = null,
    val apiCiudades: List<Ciudad> = emptyList()
)