package com.billetera.appdelclima

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "clima_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        val CIUDAD_KEY = stringPreferencesKey("ultima_ciudad")
    }

    suspend fun guardarCiudad(ciudad: String) {
        context.dataStore.edit { prefs ->
            prefs[CIUDAD_KEY] = ciudad
        }
    }

    fun obtenerCiudad(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[CIUDAD_KEY]
        }
    }
}