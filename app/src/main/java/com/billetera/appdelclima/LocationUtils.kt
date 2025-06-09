package com.billetera.appdelclima

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

object LocationUtils {
    @SuppressLint("MissingPermission")
    suspend fun obtenerCiudadDesdeUbicacion(context: Context): String? = withContext(Dispatchers.IO) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        location?.let {
            val geocoder = Geocoder(context, Locale.getDefault())
            val result = geocoder.getFromLocation(it.latitude, it.longitude, 1)
            return@withContext result?.firstOrNull()?.locality
        }
        return@withContext null
    }
}