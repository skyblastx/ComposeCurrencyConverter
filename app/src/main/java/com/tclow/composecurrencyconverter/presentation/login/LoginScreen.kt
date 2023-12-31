package com.tclow.composecurrencyconverter.presentation.login

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoNotDisturbAlt
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tclow.composecurrencyconverter.presentation.login.model.LoginViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tclow.composecurrencyconverter.utils.Screen
import com.tclow.composecurrencyconverter.utils.data.LayoutInformation
import com.tclow.composecurrencyconverter.utils.data.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    layoutInformation: LayoutInformation,
    users: List<Users>,
    onNavigate: (currentUser: Users) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {

    var userId by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var showPassword by remember {
        mutableStateOf(false)
    }

    var authState by remember {
        mutableStateOf(mapOf("" to ""))
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = "Login") },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }) { paddingValues ->

        LaunchedEffect(key1 = Unit) {
            viewModel.loginState.collect { event ->
                when (event) {
                    is LoginEvent.Success -> {
                        onNavigate(event.user)
                    }

                    is LoginEvent.Failure -> {
                        authState = mapOf(event.source to event.errorMsg)
                    }

                    else -> {}
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        }
                    )
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(modifier = Modifier.fillMaxWidth(0.8F),
                value = userId,
                onValueChange = { userId = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                isError = authState.containsKey("ID"),
                supportingText = {
                    if (authState.containsKey("ID")) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = authState.getValue("ID"),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                label = { Text("User ID") })

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.8F),
                singleLine = true,
                value = password,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                onValueChange = {
                    // Limit PIN length to 6 characters
                    if (it.length <= 6) password = it
                },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        keyboardController?.hide()
                        // Login
                        coroutineScope.launch {
                            viewModel.validateUser(
                                userID = userId,
                                password = password,
                                users = users
                            )
                        }
                    }
                ),
                isError = authState.containsKey("PIN"),
                supportingText = {
                    if (authState.containsKey("PIN")) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = authState.getValue("PIN"),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                trailingIcon = {
                    val icon =
                        if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = {
                        showPassword = !showPassword
                    }) {
                        Icon(imageVector = icon, contentDescription = "")
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            CustomRow(
                layoutInformation = layoutInformation,
                coroutineScope = coroutineScope,
                users = users,
                userId = userId,
                password = password
            )

            Spacer(modifier = Modifier.height(50.dp))

            AsyncImage(
                model = layoutInformation.layoutData.imageUrl,
                contentDescription = "Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp
                    )
            )
        }
    }
}


@Composable
fun CustomRow(
    viewModel: LoginViewModel = hiltViewModel(),
    layoutInformation: LayoutInformation,
    coroutineScope: CoroutineScope,
    users: List<Users>,
    userId: String,
    password: String
) {
    val openUrlLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // No need to handle result in demo
    }

    Row(
        modifier = Modifier.fillMaxWidth(0.8F),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        if (layoutInformation.layoutMeta.hasAboutApp) {
            OutlinedButton(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(layoutInformation.layoutData.aboutAppUrl)
                    )
                    openUrlLauncher.launch(intent)
                }, modifier = Modifier.width(120.dp), shape = RoundedCornerShape(percent = 50)
            ) {
                Text(
                    text = "About App", color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(20.dp))
        }

        OutlinedButton(
            onClick = {
                // Login
                coroutineScope.launch {
                    viewModel.validateUser(
                        userID = userId,
                        password = password,
                        users = users
                    )
                }
            },
            modifier = Modifier.width(120.dp),
            shape = RoundedCornerShape(percent = 50),
            border = BorderStroke(1.dp, layoutInformation.layoutMeta.btnLoginColor)
        ) {
            Text(
                text = "Login", color = layoutInformation.layoutMeta.btnLoginColor
            )
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    //LoginScreen()
}