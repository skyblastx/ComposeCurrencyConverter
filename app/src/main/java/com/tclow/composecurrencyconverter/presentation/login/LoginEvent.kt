package com.tclow.composecurrencyconverter.presentation.login

sealed class LoginEvent {
    class Success(val resultMsg: String): LoginEvent()
    class Failure(val source: String, val errorMsg: String): LoginEvent()
    object Empty: LoginEvent()
}
