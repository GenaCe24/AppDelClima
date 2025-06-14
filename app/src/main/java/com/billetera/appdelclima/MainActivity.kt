package com.billetera.appdelclima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.billetera.appdelclima.ui.ciudad.CiudadViewModel

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