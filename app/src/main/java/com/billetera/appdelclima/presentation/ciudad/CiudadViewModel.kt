package com.billetera.appdelclima.ui.ciudad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billetera.appdelclima.StoredCity
import com.billetera.appdelclima.repository.Repositorio
import com.billetera.appdelclima.repository.RepositorioApi
import com.billetera.appdelclima.repository.modelos.Ciudad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CiudadViewModel(
    private val repositorio: Repositorio = RepositorioApi()
) : ViewModel() {

    private val _state = MutableStateFlow(CiudadState())
    val state: StateFlow<CiudadState> = _state

    fun onIntent(intent: CiudadIntent) {
        when (intent) {
            is CiudadIntent.LoadCiudades -> {
                //guardar las últimas ciudades buscadas.
            }
            is CiudadIntent.BuscarCiudad -> {
                val query = intent.query
                if (query.isBlank()) {
                    _state.update { it.copy(ciudades = emptyList(), buscando = false, error = null) }
                    return
                }

                viewModelScope.launch {
                    _state.update { it.copy(buscando = true, error = null, ciudades = emptyList()) }
                    try {
                        val resultadoCiudades = repositorio.buscarCiudad(query)
                        _state.update {
                            it.copy(
                                buscando = false,
                                ciudades = resultadoCiudades
                            )
                        }
                    } catch (e: Exception) {
                        _state.update {
                            it.copy(
                                buscando = false,
                                error = "Error al buscar ciudades: ${e.message}"
                            )
                        }
                    }
                }
            }

            is CiudadIntent.SeleccionarCiudad -> {
                _state.update { it.copy(ciudadSeleccionada = intent.ciudad) }
            }

            //TODO: teriminar logica por ubicacion.
            is CiudadIntent.BuscarPorUbicacion -> {
                val ciudadFija = StoredCity(0.0f, 0.0f, "Buenos Aires")
                _state.update { it.copy(ciudadSeleccionada = ciudadFija) }
            }
            else -> {}
        }
    }
}