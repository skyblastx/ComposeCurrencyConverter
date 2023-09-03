package com.tclow.composecurrencyconverter.presentation.login

import com.tclow.composecurrencyconverter.utils.data.Users

sealed class LoginEvent {
    class Success(val user: Users): LoginEvent()
    class Failure(val source: String, val errorMsg: String): LoginEvent()
    object Empty: LoginEvent()
}
