package com.tclow.composecurrencyconverter.presentation.login

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tclow.composecurrencyconverter.presentation.login.model.LoginViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {

    var userId by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var showPassword by remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val layoutInformation by viewModel.layoutInformationFlow.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxWidth(),
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
            label = { Text("User ID") })

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(modifier = Modifier.fillMaxWidth(0.8F),
            singleLine = true,
            value = password,
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() }),
            trailingIcon = {
                val icon = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = {
                    showPassword = !showPassword
                }) {
                    Icon(imageVector = icon, contentDescription = "")
                }
            })

        Spacer(modifier = Modifier.height(20.dp))

        //CustomRow(layoutInformation = layoutInformation!!)
    }
}

data class LayoutMeta(
    val hasAboutApp: Boolean
)

data class LayoutInformation(
    val layoutMeta: LayoutMeta,
    val layoutData: LoginViewModel.Data
)

@Composable
fun CustomRow(
    layoutInformation: LayoutInformation
) {
    val openUrlLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle result if needed
    }

    Row(
        modifier = Modifier.fillMaxWidth(0.8F),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        if (layoutInformation.layoutMeta.hasAboutApp)
        {
            Button(
                modifier = Modifier.width(120.dp),
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(layoutInformation.layoutData.aboutAppUrl))
                    openUrlLauncher.launch(intent)
                }) {
                Text(text = "About App")
            }

            Spacer(modifier = Modifier.width(20.dp))
        }

        Button(
            modifier = Modifier.width(120.dp),
            onClick = {
                // TODO: Handle Login click
            }) {
            Text(text = "Login")
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    LoginScreen()
}