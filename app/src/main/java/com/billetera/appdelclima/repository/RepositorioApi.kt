package com.billetera.appdelclima.repository

import com.billetera.appdelclima.repository.modelos.Ciudad
import com.billetera.appdelclima.repository.modelos.Clima
import com.billetera.appdelclima.repository.modelos.ForecastDTO
import com.billetera.appdelclima.repository.modelos.ListForecast
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class RepositorioApi : Repositorio {
    private val apiKey = "b8d9829d38209664911e678cd87a87a7"

    private val cliente = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override suspend fun buscarCiudad(ciudad: String): List<Ciudad> {
        val respuesta = cliente.get("https://api.openweathermap.org/geo/1.0/direct") {
            parameter("q", ciudad)
            parameter("limit", 100)
            parameter("appid", apiKey)
        }

        if (respuesta.status == HttpStatusCode.OK) {
            return respuesta.body()
        } else {
            throw Exception("Error al buscar ciudad")
        }
    }

    override suspend fun traerClima(lat: Float, lon: Float, unidad: String): Clima {
        val respuesta = cliente.get("https://api.openweathermap.org/data/2.5/weather") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("units", unidad)
            parameter("appid", apiKey)
        }

        if (respuesta.status == HttpStatusCode.OK) {
            return respuesta.body()
        } else {
            throw Exception("Error al obtener clima actual")
        }
    }

    override suspend fun traerPronostico(nombre: String, unidad: String): List<ListForecast> {
        val respuesta = cliente.get("https://api.openweathermap.org/data/2.5/forecast") {
            parameter("q", nombre)
            parameter("units", unidad)
            parameter("appid", apiKey)
        }

        if (respuesta.status == HttpStatusCode.OK) {
            val forecast = respuesta.body<ForecastDTO>()
            return forecast.list
        } else {
            throw Exception("Error al obtener pronóstico")
        }
    }
}
