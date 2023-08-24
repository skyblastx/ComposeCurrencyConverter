package com.tclow.composecurrencyconverter.presentation

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.tclow.composecurrencyconverter.presentation.convert.ConvertScreen
import com.tclow.composecurrencyconverter.presentation.login.LoginScreen
import com.tclow.composecurrencyconverter.presentation.model.MainViewModel
import com.tclow.composecurrencyconverter.ui.theme.ComposeCurrencyConverterTheme
import com.tclow.composecurrencyconverter.utils.Screen
import com.tclow.composecurrencyconverter.utils.navigation.CustomNavHost
import com.tclow.composecurrencyconverter.utils.navigation.NavigationIntent
import com.tclow.composecurrencyconverter.utils.navigation.composable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

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
fun CurrencyConverterApp(viewModel: MainViewModel = hiltViewModel()) {
    ComposeCurrencyConverterTheme {
        val navController = rememberNavController()

        NavigationEff(
            navigationChannel = viewModel.navigationChannel,
            navHostController = navController
        )

        CustomNavHost(
            navController = navController,
            startDestination = Screen.Login
        ) {
//            composable(Screen.Splash) {
//                SplashScreen()
//            }

            composable(Screen.Login) {
                LoginScreen()
            }

            composable(Screen.Convert) {
                ConvertScreen()
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}

@Composable
fun NavigationEff(
    navigationChannel: Channel<NavigationIntent>,
    navHostController: NavHostController
) {
    val activity = LocalContext.current as? Activity
    val focusManager = LocalFocusManager.current

    LaunchedEffect(activity, navHostController, navigationChannel) {
        navigationChannel.receiveAsFlow().collect { intent ->
            if (activity?.isFinishing == true)
            {
                return@collect
            }

            when (intent)
            {
                is NavigationIntent.NavigateBack -> {
                    focusManager.clearFocus()

                    if(intent.route != null)
                    {
                        navHostController.popBackStack(intent.route,true)
                    }
                    else
                    {
                        navHostController.popBackStack()
                    }
                }

                is NavigationIntent.Navigate -> {
                    navHostController.navigate(intent.route)
                }
            }
        }
    }
}