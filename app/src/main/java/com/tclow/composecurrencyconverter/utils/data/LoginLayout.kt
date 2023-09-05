package com.tclow.composecurrencyconverter.utils.data

import androidx.compose.ui.graphics.Color

data class Data(
    val aboutAppUrl: String = "",
    val imageUrl: String = ""
)

data class Meta(
    val mode: String = ""
)

data class LayoutMeta(
    val btnLoginColor: Color = Color.Black,
    val hasAboutApp: Boolean
)

data class LayoutInformation(
    val layoutMeta: LayoutMeta,
    val layoutData: Data
)
