package com.tclow.composecurrencyconverter.presentation.model

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.tclow.composecurrencyconverter.utils.Screen
import com.tclow.composecurrencyconverter.utils.connection.ConnectivityObserver
import com.tclow.composecurrencyconverter.utils.connection.ConnectivityObserverImpl
import com.tclow.composecurrencyconverter.utils.data.Data
import com.tclow.composecurrencyconverter.utils.data.LayoutInformation
import com.tclow.composecurrencyconverter.utils.data.LayoutMeta
import com.tclow.composecurrencyconverter.utils.data.Meta
import com.tclow.composecurrencyconverter.utils.data.UserInfo
import com.tclow.composecurrencyconverter.utils.data.Users
import com.tclow.composecurrencyconverter.utils.navigation.CustomNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    customNavigation: CustomNavigation,
    private val application: Application,
    private val database: FirebaseDatabase
): ViewModel() {
    private val navigator = customNavigation
    val navigationChannel = customNavigation.navigationChannel

    //============================================
    // Layout Info Flow
    //============================================

    private val dataNode = database.getReference("ui/data")
    private val layoutNode = database.getReference("ui/layout")
    private val metaNode = database.getReference("ui/meta")

    private val _dataFlow: Flow<Data> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = Data().copy(
                    aboutAppUrl = snapshot.children.find {
                        it.key == "aboutAppUrl"
                    }!!.value.toString()
                )
                trySend(data)
            }

            override fun onCancelled(error: DatabaseError) {
                // Ignored for demo
            }

        }

        dataNode.addValueEventListener(listener)
        awaitClose { dataNode.removeEventListener(listener) }
    }
    private val _layoutFlow: Flow<Map<String, Boolean>> = callbackFlow {

        fun parse(snapshot: DataSnapshot): Boolean {
            val test = snapshot.children.find {
                it.key == "hasAboutApp"
            }!!.getValue<Boolean>()!!
            return test
        }

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val map = snapshot.children.associate {
                    it.key!! to parse(it)
                }
                trySend(map)
            }

            override fun onCancelled(error: DatabaseError) {
                // Ignored for demo
            }
        }

        layoutNode.addValueEventListener(listener)
        awaitClose { layoutNode.removeEventListener(listener) }
    }
    private val _metaFlow: Flow<Meta> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue<Meta>()!!)
            }

            override fun onCancelled(error: DatabaseError) {
                // Ignored for demo
            }

        }

        metaNode.addValueEventListener(listener)
        awaitClose { metaNode.removeEventListener(listener) }
    }

    val layoutInformationFlow: StateFlow<LayoutInformation?> = combine(
        _dataFlow, _layoutFlow, _metaFlow
    ) { data, layout, meta ->
        val layoutMeta = LayoutMeta(
            hasAboutApp = layout[meta.mode] ?: true
        )
        return@combine LayoutInformation(
            layoutMeta = layoutMeta,
            layoutData = data
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    //============================================
    // User Info Flow
    //============================================

    private val _userNode = database.getReference("users")
    val userNode = _userNode

    val userFlow: StateFlow<List<Users>?> = callbackFlow {
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
                val userList = snapshot.children.map {
                    val user = Users().copy(
                        id = it.key ?: "",
                        userInfo = parse(it)
                    )
                    user
                }

                trySend(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Ignored
            }
        }

        _userNode.addValueEventListener(listener)
        awaitClose { _userNode.removeEventListener(listener) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private var currentUser = Users()

    fun getCurrentUser(): Users {
        return currentUser
    }

    fun setCurrentUser(user: Users) {
        // Set logged in status
        userNode.child(user.id).child("isLoggedIn").setValue(true)

        this.currentUser = user
//        savedStateHandle["user"] = this.currentUser.id
    }

    fun logout() {
        userNode.child(currentUser.id).child("isLoggedIn").setValue(false)
        this.currentUser = Users()
//        savedStateHandle["user"] = ""

        navigator.navigate(
            route = Screen.Login.fullRoute,
            popUpToRoute = Screen.Convert.fullRoute,
            inclusive = true
        )
    }

    //============================================
    // Functions
    //============================================

    suspend fun routeToLogin()
    {
//        val connectivityObserver: ConnectivityObserver = ConnectivityObserverImpl(application.applicationContext)
//
//        connectivityObserver.observe().collect {
//            Toast.makeText(application.applicationContext, it.toString(), Toast.LENGTH_SHORT).show()
//        }

//        val userID = savedStateHandle.get<String>("user")

        layoutInformationFlow.collect {
            if (layoutInformationFlow.value != null) {
                navigator.navigate(
                    route = Screen.Login.fullRoute,
                    popUpToRoute = Screen.Splash.fullRoute,
                    inclusive = true
                )
                cancel()
            }
        }
    }

    fun routeToConvert()
    {
        navigator.navigate(
            route = Screen.Convert.fullRoute,
            popUpToRoute = Screen.Login.fullRoute,
            inclusive = true
        )
    }
}