package com.tclow.composecurrencyconverter.presentation.login.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.tclow.composecurrencyconverter.presentation.login.LayoutInformation
import com.tclow.composecurrencyconverter.presentation.login.LayoutMeta
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val database: FirebaseDatabase
) : ViewModel() {
    private val dataNode = database.getReference("ui/data")
    private val layoutNode = database.getReference("ui/layout")
    private val metaNode = database.getReference("ui/meta")

    data class Data(val aboutAppUrl: String = "")

    data class Meta(val mode: String = "")

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
}