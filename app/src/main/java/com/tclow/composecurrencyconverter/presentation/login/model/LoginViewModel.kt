package com.tclow.composecurrencyconverter.presentation.login.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.tclow.composecurrencyconverter.presentation.login.LoginEvent
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
    private val navigation: CustomNavigation,
    private val database: FirebaseDatabase
) : ViewModel() {
    private val userNode = database.getReference("users")

    private val _userFlow: Flow<Users> = callbackFlow {
        fun parse(snapshot: DataSnapshot): UserInfo {
            val userInfo = UserInfo().copy(
                pin = snapshot.children.find {
                    it.key == "pin"
                }!!.value.toString(),
                username = snapshot.children.find {
                    it.key == "username"
                }!!.value.toString(),
                isLoggedIn = snapshot.children.find {
                    it.key == "isLoggedIn"
                }!!.getValue<Boolean>()!!
            )
            return userInfo
        }

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = Users().copy(
                    id = snapshot.children.find {
                        it.key == "ss"
                    }!!.key.toString(),
                    userInfo = parse(snapshot.children.find {
                        it.key == "ss"
                    }!!)
                )
                trySend(user)
            }

            override fun onCancelled(error: DatabaseError) {
                // Ignored
            }
        }

        userNode.addValueEventListener(listener)
        awaitClose { userNode.removeEventListener(listener) }
    }

    private val _loginState = MutableStateFlow<LoginEvent>(LoginEvent.Empty)
    val loginState: StateFlow<LoginEvent> = _loginState

    suspend fun validateUser(userID: String, password: String) {
        _userFlow.collect {
            Log.d("LoginViewModel", "User ID: ${it.id}, User PIN: ${it.userInfo?.pin}, Username: ${it.userInfo?.username}, isLoggedIn: ${it.userInfo?.isLoggedIn}")
            if (userID != it.id)
            {
                _loginState.value = LoginEvent.Failure("ID", "Incorrect ID")
            }
            else if (password != it.userInfo?.pin)
            {
                _loginState.value = LoginEvent.Failure("PIN", "Incorrect PIN")
            }
            else if (userID == it.id && password == it.userInfo.pin)
            {
                userNode.child(userID).child("isLoggedIn").setValue(true)
                _loginState.value = LoginEvent.Success("Successful login")
            }
        }
    }

    fun routeToConvert()
    {
        navigation.navigate(
            route = Screen.Convert.fullRoute,
            popUpToRoute = Screen.Login.fullRoute,
            inclusive = true
        )
    }
}