package com.tclow.composecurrencyconverter.utils.data

data class Users(
    val id: String = "",
    val userInfo: UserInfo? = null
)

data class UserInfo(
    val pin: String = "",
    val username: String = "",
    val isLoggedIn: Boolean = false
)
