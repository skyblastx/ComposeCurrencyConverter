package com.tclow.composecurrencyconverter.utils.navigation

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomNavigationImpl @Inject constructor() : CustomNavigation {
    override val navigationChannel = Channel<NavigationIntent>(
        capacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    override fun navigateBack(route: String?, inclusive:Boolean) {
        navigationChannel.trySend(
            NavigationIntent.NavigateBack(route,inclusive)
        )
    }

    override fun navigate(route: String) {
        navigationChannel.trySend(
            NavigationIntent.Navigate(route)
        )
    }
}