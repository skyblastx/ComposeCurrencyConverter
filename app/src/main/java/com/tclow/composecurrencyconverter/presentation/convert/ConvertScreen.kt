package com.tclow.composecurrencyconverter.presentation.convert

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.owlbuddy.www.countrycodechooser.CountryCodeChooser
import com.owlbuddy.www.countrycodechooser.utils.enums.CountryCodeType
import com.tclow.composecurrencyconverter.presentation.convert.model.ConvertViewModel
import java.util.Currency
import java.util.Locale

private data class ResultWithColor(
    val result: String,
    val color: Color
)

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ConvertScreen(
    viewModel: ConvertViewModel = hiltViewModel()
) {
    var strAmount by remember {
        mutableStateOf("")
    }

    var strFromCurrency by remember {
        mutableStateOf("MYR")
    }

    var strToCurrency by remember {
        mutableStateOf("USD")
    }

    var result by remember {
        mutableStateOf(ResultWithColor("test", Color.Black))
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text( text = "Currency Converter" ) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                navigationIcon = {
                    TextButton(
                        onClick = {
                            // TODO: logout
                            viewModel.logout()
                        }
                    ) {
                        Text(
                            text = "Logout",
                            color = Color.Red
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        LaunchedEffect(key1 = Unit) {
            viewModel.getRates()

            viewModel.conversion.collect { event ->
                when (event) {
                    is ConvertEvent.Success -> {
                        isLoading = false
                        result = result.copy(
                            result = event.resultMsg,
                            color = Color.Black
                        )
                    }

                    is ConvertEvent.Failure -> {
                        isLoading = false
                        result = result.copy(
                            result = event.errorMsg,
                            color = Color.Red
                        )
                    }

                    is ConvertEvent.Loading -> {
                        isLoading = true
                    }

                    else -> Unit
                }
            }
        }

        Column (
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
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                fontSize = 30.sp,
                text = "Welcome back, user"
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                TextField(
                    value = strAmount,
                    modifier = Modifier.fillMaxWidth(0.4f),
                    onValueChange = { strAmount = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                    ),
                    label = { Text("Amount") }
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("From")

                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(35.dp)
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(5.dp),
                                color = Color.Gray
                            )
                    ) {
                        Row(
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            CountryCodeChooser(
                                defaultCountry = "MY",
                                countryCodeType = CountryCodeType.FLAG,
                                onCountySelected = { _, iso2Code, _ ->
                                    val locale = Locale("", iso2Code)
                                    val currency = Currency.getInstance(locale).currencyCode
                                    strFromCurrency = currency
                                }
                            )

                            Spacer(modifier = Modifier.width(3.dp))

                            Text (
                                text = strFromCurrency,
                                color = Color.Black
                            )
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("To")

                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(35.dp)
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(5.dp),
                                color = Color.Gray
                            )
                    ) {
                        Row (
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            CountryCodeChooser(
                                defaultCountry = "US",
                                countryCodeType = CountryCodeType.FLAG,
                                onCountySelected = { _, iso2Code, _ ->
                                    val locale = Locale("", iso2Code)
                                    val currency = Currency.getInstance(locale).currencyCode
                                    strToCurrency = currency
                                }
                            )

                            Spacer(modifier = Modifier.width(3.dp))

                            Text (
                                text = strToCurrency,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    text = result.result,
                    color = result.color
                )

                Spacer(modifier = Modifier.weight(1f))

                OutlinedButton(
                    onClick = {
                        // TODO: Handle conversion click
                        viewModel.convert(
                            strAmount = strAmount,
                            fromCurrency = strFromCurrency,
                            toCurrency = strToCurrency)
                    }
                ) {
                    Text(
                        text = "Convert",
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ConvertPreview() {
    ConvertScreen()
}