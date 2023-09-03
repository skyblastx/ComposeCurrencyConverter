package com.tclow.composecurrencyconverter.presentation.login.model

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.tclow.composecurrencyconverter.presentation.login.LoginEvent
import com.tclow.composecurrencyconverter.presentation.model.MainViewModel
import com.tclow.composecurrencyconverter.utils.Screen
import com.tclow.composecurrencyconverter.utils.data.UserInfo
import com.tclow.composecurrencyconverter.utils.data.Users
import com.tclow.composecurrencyconverter.utils.navigation.CustomNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navigation: CustomNavigation
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginEvent>(LoginEvent.Empty)
    val loginState: StateFlow<LoginEvent> = _loginState

    fun validateUser(userID: String, password: String, users: List<Users>) {
        for (user in users) {
            Log.d("LoginViewModel", "user id: ${user.id}, typed in $userID")
            if (userID != user.id) {
                _loginState.value = LoginEvent.Failure("ID", "Incorrect ID")
                break
            } else {
                if (password != user.userInfo?.pin) {
                    _loginState.value = LoginEvent.Failure("PIN", "Incorrect PIN")
                    break
                }
                else {
                    _loginState.value = LoginEvent.Success(user)
                    break
                }
            }
        }
    }
}