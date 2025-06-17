package com.billetera.appdelclima

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "clima_prefs")

data class StoredCity (
    val lat : Float,
    val long: Float,
    val name: String
)

class DataStoreManager(private val context: Context) {

    companion object {
        val NAME_KEY = stringPreferencesKey("name_ultima_ciudad")
        val LATITUDE_KEY = floatPreferencesKey("latitude_ultima_ciudad")
        val LONGITUDE_KEY = floatPreferencesKey("longitude_ultima_ciudad")
        val UNIT_KEY = stringPreferencesKey("unidad_temperatura") // "metric" o "imperial"

    }

    suspend fun guardarCiudad(lat: Float = 0f, long: Float = 0f, name: String = "Ciudad Desconocida") {
        println("Guardando ciudad en store: $name, lat: $lat, long: $long")

        context.dataStore.edit { prefs ->
            prefs[NAME_KEY] = name
            prefs[LATITUDE_KEY] = lat
            prefs[LONGITUDE_KEY] = long
        }
    }

    suspend fun guardarUnidad(unidad: String) {
        context.dataStore.edit { prefs ->
            prefs[UNIT_KEY] = unidad
        }
    }


    fun obtenerUnidad(): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[UNIT_KEY] ?: "metric" // valor por defecto
        }
    }


    fun obtenerCiudad(): Flow<StoredCity?> {
        return context.dataStore.data.map { prefs ->
            val lat = prefs[LATITUDE_KEY]
            val long = prefs[LONGITUDE_KEY]
            val name = prefs[NAME_KEY]
            if (lat != null && long != null && name != null) {
                StoredCity(lat, long, name)
            } else {
                null
            }
        }
    }
}