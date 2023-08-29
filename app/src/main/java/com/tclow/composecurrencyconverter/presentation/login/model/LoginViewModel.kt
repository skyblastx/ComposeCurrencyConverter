package com.tclow.composecurrencyconverter.presentation.login.model

import androidx.lifecycle.ViewModel
import com.tclow.composecurrencyconverter.utils.Screen
import com.tclow.composecurrencyconverter.utils.navigation.CustomNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navigation: CustomNavigation
) : ViewModel() {
    // TODO: user validation

    fun validateUser(userID: String, password: String) {

    }

    fun route(screen: Screen)
    {
        navigation.navigate(
            route = screen.fullRoute,
            popUpToRoute = screen.fullRoute
        )
    }
}