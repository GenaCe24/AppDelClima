package com.billetera.appdelclima.ui.ciudad

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CiudadViewModel : ViewModel() {

    private val ciudadesMock = listOf("Buenos Aires", "Córdoba", "Mendoza", "Salta", "Rosario")

    private val _state = MutableStateFlow(CiudadState())
    val state: StateFlow<CiudadState> = _state

    fun onIntent(intent: CiudadIntent) {
        when (intent) {
            is CiudadIntent.LoadCiudades -> {
                _state.update { it.copy(ciudades = ciudadesMock) }
            }
            is CiudadIntent.BuscarCiudad -> {
                val filtradas = ciudadesMock.filter {
                    it.contains(intent.query, ignoreCase = true)
                }
                _state.update { it.copy(ciudades = filtradas) }
            }
            is CiudadIntent.SeleccionarCiudad -> {
                _state.update { it.copy(ciudadSeleccionada = intent.nombre) }
            }
            else -> {}
        }
    }
}