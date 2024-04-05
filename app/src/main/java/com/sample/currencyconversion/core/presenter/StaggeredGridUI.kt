package com.sample.currencyconversion.core.presenter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.sample.currencyconversion.core.data.dto.CurrencyDTO
import com.sample.currencyconversion.ui.theme.Gray90
import com.sample.currencyconversion.ui.theme.MyTheme

@Composable
fun StaggeredGrid(currencyList: List<CurrencyDTO>, baseValue: Double, inputValue: Double) {

    Spacer(modifier = Modifier.height(60.dp))

    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp),
        contentPadding = PaddingValues(0.dp),
        columns = StaggeredGridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalItemSpacing = 10.dp

        //columns = GridCells.Adaptive(minSize = 120.dp), // Adjust minSize for your needs

    ) {
        items(currencyList.size) {
            val updatedValue = (currencyList[it].currencyValue / baseValue) * inputValue
            StaggeredCell(currencyDTO = currencyList[it], updatedValue)
        }
    }


}

@Composable
fun StaggeredCell(currencyDTO: CurrencyDTO, updatedValue: Double) {
    Column(
        modifier = Modifier
            .background(Gray90, shape = RoundedCornerShape(15.dp))
            .fillMaxSize()
            .padding(start = 10.dp, bottom = 20.dp, end = 10.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = currencyDTO.currencyCode,
            style = MyTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = updatedValue.toString(),
            style = MyTheme.typography.bodySmall,
            modifier = Modifier.semantics {
                contentDescription = "currency value"
            }
        )
    }
}