package com.billetera.appdelclima

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.billetera.appdelclima.navigation.AppNavigation
import com.billetera.appdelclima.ui.ciudad.CiudadViewModel

@Composable
fun MainPage(dataStore: DataStoreManager, viewModel: CiudadViewModel) {
    val navController: NavHostController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    MaterialTheme {
        Surface {
            AppNavigation(
                navController = navController,
                coroutineScope = coroutineScope,
                ciudadViewModel = viewModel,
                dataStore = dataStore
            )
        }
    }
}