package com.sample.currencyconversion.core.presenter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sample.currencyconversion.R
import com.sample.currencyconversion.core.data.dto.CurrencyDTO
import com.sample.currencyconversion.ui.theme.Gray60
import com.sample.currencyconversion.ui.theme.Gray70
import com.sample.currencyconversion.ui.theme.MyTheme
import com.sample.currencyconversion.ui.theme.Purple40
import com.sample.currencyconversion.ui.theme.padding15
import com.sample.currencyconversion.ui.theme.padding25

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRateContent(currencyList: MutableState<List<CurrencyDTO>>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(
                start = padding15,
                top = padding25,
                end = padding15
            ),
    ) {

        var bottomSheetState by rememberSaveable {
            mutableStateOf(false)
        }
        val sheetState = rememberModalBottomSheetState()

        val baseCurrencyCodeState = remember {
            mutableStateOf("USD")
        }

        var baseCurrencyValue by remember { mutableDoubleStateOf(1.0) }

        var inputCurrencyValue by remember { mutableDoubleStateOf(1.0) }

        // update currency value by base value

        val buttonEnableState = remember {
            mutableStateOf(false)
        }

        buttonEnableState.value = currencyList.value.isNotEmpty()

        InputCurrencyContent {
            if (it.isNotEmpty()) {
                inputCurrencyValue = 1.0
                inputCurrencyValue = it.toDouble()
            } else {
                inputCurrencyValue = 0.0
                inputCurrencyValue = 0.0
            }

        }

        CurrencyButton(baseCurrencyCodeState.value, buttonEnableState.value) {
            bottomSheetState = true
        }

        if (currencyList.value.isNotEmpty()) {
            StaggeredGrid(currencyList = currencyList.value, baseCurrencyValue, inputCurrencyValue)
        } else {
            CenteredProgressBar()
        }

        // bottom sheet section
        if (bottomSheetState) {

            ExchangeRateModalBottomSheet(currencyList.value, sheetState, {
                // onClose
                bottomSheetState = false
            }) { currency ->
                // get the selected currency code and the currency value from the bottom sheet
                baseCurrencyCodeState.value = currency.currencyCode
                baseCurrencyValue = 1.0 // reset the value before assign
                baseCurrencyValue = currency.currencyValue
            }
        }
    }

}

@Composable
fun CenteredProgressBar() {
    Row(
        modifier = Modifier
            .fillMaxSize(), // Fill the entire available space
        horizontalArrangement = Arrangement.Center // Center elements horizontally
    ) {
        ProgressBar() // Call the composable for the progress bar
    }
}

@Composable
fun ProgressBar() {
    Spacer(modifier = Modifier.height(200.dp))
    CircularProgressIndicator(
        modifier = Modifier
            .size(60.dp)
            .padding(16.dp),
        color = Purple40
    )
}

@Composable
fun InputCurrencyContent(onTextChange: (String) -> Unit) {

    var textValueState by remember { mutableStateOf(TextFieldValue("1")) }

    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            style = MyTheme.typography.titleLarge,
            text = stringResource(id = R.string.exchange_rates),

            )

        Text(
            style = MyTheme.typography.titleSmall,
            text = "Test",

            )
    }


    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier
            .border(BorderStroke(2.dp, Gray70), RoundedCornerShape(20.dp))
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically


    ) {
        BasicTextField(
            value = textValueState,
            onValueChange = {
                textValueState = it
                onTextChange(textValueState.text)
            },

            textStyle = TextStyle(color = Gray60, textAlign = TextAlign.End, fontSize = 18.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp)
                .onFocusEvent { event ->
                    if (event.isFocused) {
                        textValueState = textValueState.copy(
                            selection = TextRange(textValueState.text.length)
                        ) // Set selection to the end
                    }
                },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
    Spacer(modifier = Modifier.height(10.dp))

}

@Composable
fun CurrencyButton(label: String, isButtonEnabled: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {

        Button(
            enabled = isButtonEnabled,
            onClick = {
                onClick()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple40, // Set the desired background color
                contentColor = Color.White, // Set the text color
            ),
            shape = RoundedCornerShape(15.dp),


            ) {
            Text(modifier = Modifier                                
                .width(60.dp)
                .semantics {
                    contentDescription = "currency button"
                }, text = label, fontSize = 14.sp
            )
            Icon(
                modifier = Modifier
                    .size(24.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_expand),
                contentDescription = null
            )
        }
    }
}
