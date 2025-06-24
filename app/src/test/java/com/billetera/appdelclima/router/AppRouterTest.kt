package com.billetera.appdelclima.router

import android.net.Uri
import androidx.navigation.NavHostController
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Test

class AppRouterTest {

    @Test
    fun testNavigateToSelectCity() {
         val navController = mockk<NavHostController>()
         every { navController.navigate(Routes.SelectCity.route) } returns Unit
         val appRouter = AppRouter(navController)

         appRouter.navigateTo(Routes.SelectCity)

         verify { navController.navigate(Routes.SelectCity.route) }
    }

    @Test
    fun testNavigateToSettings() {
        val navController = mockk<NavHostController>()
        every { navController.navigate(Routes.Settings.route) } returns Unit
        val appRouter = AppRouter(navController)

        appRouter.navigateTo(Routes.Settings)

        verify { navController.navigate(Routes.Settings.route) }
    }

    @Test
    fun testNavigateToShowWeather() {
        val navController = mockk<NavHostController>()
        mockkStatic(Uri::class)
        every { Uri.encode("Calamuchita") } returns "Calamuchita"
        val locLat = 22.0f
        val locLong = 33.0f
        val locName = "Calamuchita"
        val expectedRoute = Routes.ShowWeather.createRoute(locLat, locLong, locName)
        every { navController.navigate(expectedRoute) } returns Unit
        val appRouter = AppRouter(navController)

        appRouter.navigateTo(Routes.ShowWeather(locLat, locLong, locName))

        verify { navController.navigate(expectedRoute) }
    }

    @Test
    fun testNavigateBack() {
        val navController = mockk<NavHostController>()
        every { navController.popBackStack() } returns true
        val appRouter = AppRouter(navController)

        appRouter.navigateBack()

        verify { navController.popBackStack() }
    }

    @Test
    fun testGetRouteForSelectCity() {
        val navController = mockk<NavHostController>()
        val appRouter = AppRouter(navController)

        val route = appRouter.getRoute(Routes.SelectCity)

        assert(route == Routes.SelectCity.route)
    }

    @Test
    fun testGetRouteForSettings() {
        val navController = mockk<NavHostController>()
        val appRouter = AppRouter(navController)

        val route = appRouter.getRoute(Routes.Settings)

        assert(route == Routes.Settings.route)
    }

    @Test
    fun testGetRouteForShowWeather() {
        val navController = mockk<NavHostController>()
        mockkStatic(Uri::class)
        every { Uri.encode("Calamuchita") } returns "Calamuchita"
        val locLat = 22.0f
        val locLong = 33.0f
        val locName = "Calamuchita"
        val expectedRoute = Routes.ShowWeather.createRoute(locLat, locLong, locName)
        val appRouter = AppRouter(navController)

        val route = appRouter.getRoute(Routes.ShowWeather(locLat, locLong, locName))

        assert(route == expectedRoute)
    }
}