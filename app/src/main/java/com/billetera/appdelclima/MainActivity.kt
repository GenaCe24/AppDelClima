package com.billetera.appdelclima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.billetera.appdelclima.ui.ciudad.CiudadScreen
import com.billetera.appdelclima.ui.ciudad.CiudadViewModel
import com.billetera.appdelclima.ui.clima.ClimaView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStore = DataStoreManager(this)
        val viewModel = CiudadViewModel()

        setContent {
            MainPage(dataStore, viewModel)
        }
    }
}