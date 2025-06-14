package com.billetera.appdelclima.ui.ciudad

import com.billetera.appdelclima.StoredCity

sealed class CiudadIntent {
    object LoadCiudades : CiudadIntent()
    data class BuscarCiudad(val query: String) : CiudadIntent()
    object BuscarPorUbicacion : CiudadIntent()
    data class SeleccionarCiudad(val ciudad: StoredCity) : CiudadIntent()
}