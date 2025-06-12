package com.billetera.appdelclima.router

interface Router {
    fun navigateTo(destination: Routes)
    fun navigateBack()
}