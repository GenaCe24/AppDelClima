package com.billetera.appdelclima.ui.ciudad

sealed class CiudadIntent {
    object LoadCiudades : CiudadIntent()
    data class BuscarCiudad(val query: String) : CiudadIntent()
    object BuscarPorUbicacion : CiudadIntent()
    data class SeleccionarCiudad(val nombre: String) : CiudadIntent()
}