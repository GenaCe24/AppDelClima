package com.billetera.appdelclima.presentation.clima

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billetera.appdelclima.repository.Repositorio
import com.billetera.appdelclima.repository.RepositorioApi
import com.billetera.appdelclima.repository.modelos.ListForecast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ClimaViewModel(
    private val repositorio: Repositorio = RepositorioApi(),
    private val unidadProvider: suspend () -> String
) : ViewModel() {


    private val _state = MutableStateFlow(ClimaState())
    val state: StateFlow<ClimaState> = _state.asStateFlow()

    fun onIntent(intent: ClimaIntent) {
        when (intent) {
            is ClimaIntent.CargarClima -> cargarDatosDeClima(
                intent.lat,
                intent.lon,
                intent.cityName
            )
        }
    }

    private fun cargarDatosDeClima(lat: Float, lon: Float, cityName: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val unidad = unidadProvider()

                val climaActual = repositorio.traerClima(lat, lon, unidad)
                val pronosticoList = repositorio.traerPronostico(cityName, unidad)


                val pronosticoParaUi = procesarPronosticoParaUI(pronosticoList)

                _state.update {
                    it.copy(
                        climaActual = climaActual,
                        pronostico = pronosticoParaUi,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar el clima: ${e.message}"
                    )
                }
            }
        }
    }


    private fun procesarPronosticoParaUI(forecastList: List<ListForecast>): List<PronosticoDiaUI> {
        val dailyForecasts = mutableListOf<PronosticoDiaUI>()
        val groupedByDay = forecastList.groupBy {
            val sdf = SimpleDateFormat("EEEE", Locale("es", "ES"))
            sdf.timeZone = TimeZone.getDefault()
            sdf.format(Date(it.dt * 1000L)).replaceFirstChar { char -> char.uppercase() }
        }

        groupedByDay.forEach { (dia, forecasts) ->
            val tempsForDay = forecasts.map { it.main.temp }
            val humiditiesForDay = forecasts.map { it.main.humidity }

            val firstForecast = forecasts.firstOrNull()
            if (firstForecast != null) {
                dailyForecasts.add(
                    PronosticoDiaUI(
                        dia = dia,
                        tempMax = tempsForDay.maxOrNull()?.toInt() ?: 0,
                        tempMin = tempsForDay.minOrNull()?.toInt() ?: 0,
                        humedad = humiditiesForDay.average().toInt(),
                        estado = firstForecast.weather.firstOrNull()?.description?.replaceFirstChar { char -> char.uppercase() }
                            ?: "N/A",
                        iconUrl = "https://openweathermap.org/img/wn/${firstForecast.weather.firstOrNull()?.icon}@2x.png"
                    )
                )
            }
        }
        val order = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        return dailyForecasts.sortedBy { order.indexOf(it.dia) }
    }
}