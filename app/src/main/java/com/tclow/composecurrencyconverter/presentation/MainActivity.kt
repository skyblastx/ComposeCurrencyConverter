package com.tclow.composecurrencyconverter.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.initialize
import com.tclow.composecurrencyconverter.presentation.convert.ConvertScreen
import com.tclow.composecurrencyconverter.presentation.login.LoginScreen
import com.tclow.composecurrencyconverter.ui.theme.ComposeCurrencyConverterTheme
import com.tclow.composecurrencyconverter.utils.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        setContent {
            ComposeCurrencyConverterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CurrencyConverterApp()
                }
            }
        }
    }
}

@Composable
fun CurrencyConverterApp() {
    ComposeCurrencyConverterTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.LOGIN
        ) {
            composable(Route.LOGIN) {
                LoginScreen()
            }

            composable(Route.CONVERT) {
                ConvertScreen()
            }
        }
    }
}