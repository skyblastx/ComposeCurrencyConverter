package com.tclow.composecurrencyconverter.utils.data

data class Data(
    val aboutAppUrl: String = ""
)

data class Meta(
    val mode: String = ""
)

data class LayoutMeta(
    val hasAboutApp: Boolean
)

data class LayoutInformation(
    val layoutMeta: LayoutMeta,
    val layoutData: Data
)
