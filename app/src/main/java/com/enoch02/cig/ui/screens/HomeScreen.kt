@file:OptIn(ExperimentalMaterial3Api::class)

package com.enoch02.cig.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enoch02.cig.data.models.MainViewModel
import com.enoch02.cig.data.models.RequestBody
import com.enoch02.cig.data.util.LARGE
import com.enoch02.cig.data.util.MEDIUM
import com.enoch02.cig.data.util.SMALL

@Composable
fun HomeScreen(modifier: Modifier, mainViewModel: MainViewModel = viewModel()) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Text(text = "Prompt: ")
        OutlinedTextField(
            value = mainViewModel.prompt,
            onValueChange = { mainViewModel.prompt = it },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Amount: ${mainViewModel.amount.toInt()}")
            Slider(
                value = mainViewModel.amount,
                onValueChange = { mainViewModel.amount = it },
                valueRange = 1f..10f,
                steps = 9
            )
        }

        val sizes = listOf(SMALL, MEDIUM, LARGE)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Size: ")
            for (i in 0..2) {
                InputChip(
                    selected = mainViewModel.selectedChip == i,
                    onClick = { mainViewModel.selectedChip = i },
                    label = {
                        Text(text = sizes[i])
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    mainViewModel.generateImage(
                        RequestBody(
                            prompt = mainViewModel.prompt,
                            n = mainViewModel.amount.toInt(),
                            size = sizes[mainViewModel.selectedChip]
                        )
                    )
                },
                content = {
                    Text(text = "Generate")
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            )

            Button(
                onClick = { mainViewModel.reset() },
                content = {
                    Text(text = "Reset")
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            )
        }
    }
}