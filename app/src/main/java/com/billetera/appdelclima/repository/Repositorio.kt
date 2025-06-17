package com.billetera.appdelclima.repository

import com.billetera.appdelclima.repository.modelos.Ciudad
import com.billetera.appdelclima.repository.modelos.Clima
import com.billetera.appdelclima.repository.modelos.ListForecast

interface Repositorio {
    suspend fun buscarCiudad(ciudad: String): List<Ciudad>
    suspend fun traerClima(lat: Float, lon: Float, unidad: String): Clima
    suspend fun traerPronostico(nombre: String, unidad: String): List<ListForecast>
}