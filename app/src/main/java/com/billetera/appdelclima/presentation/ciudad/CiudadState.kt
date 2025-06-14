package com.billetera.appdelclima.ui.ciudad

import com.billetera.appdelclima.StoredCity
import com.billetera.appdelclima.repository.modelos.Ciudad

data class CiudadState(
    val ciudades: List<Ciudad> = emptyList(),
    val ciudadSeleccionada: StoredCity? = null,
    val buscando: Boolean = false,
    val error: String? = null,
)