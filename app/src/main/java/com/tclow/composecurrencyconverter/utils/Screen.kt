package com.tclow.composecurrencyconverter.utils

sealed class Screen(protected val route : String, vararg params:String)
{
    val queries : String = if (params.isEmpty()) "" else
    {
        val builder = StringBuilder()
        params.forEach { builder.append("/{${it}}")}
        builder.toString()
    }

    val fullRoute : String = if (params.isEmpty()) route else
    {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}")}
        builder.toString()
    }

    val baseRoute : String = route

    object Login : Screen("Login")

    object Splash : Screen("Splash")
    object Convert : Screen("Convert")

}