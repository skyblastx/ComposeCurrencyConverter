package com.tclow.composecurrencyconverter.di

import com.tclow.composecurrencyconverter.utils.navigation.CustomNavigation
import com.tclow.composecurrencyconverter.utils.navigation.CustomNavigationImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun bindNavigation(impl: CustomNavigationImpl): CustomNavigation
}