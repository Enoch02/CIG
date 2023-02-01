package com.enoch02.cig.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enoch02.cig.data.models.MainViewModel
import com.enoch02.cig.ui.composables.ImageItem
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ResultDialog(mainViewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = { mainViewModel.showResultDialog = false },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                content = {
                    val images = mainViewModel.getGeneratedImages()

                    Column {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Results",
                                )
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        mainViewModel.showResultDialog = false
                                        mainViewModel.reset()
                                    },
                                    content = {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Close dialog"
                                        )
                                    },
                                )
                            },
                            actions = {
                                TextButton(
                                    onClick = { mainViewModel.downloadAll(context) },
                                    content = {
                                        Text(text = "Download all")
                                    },
                                )
                            }
                        )

                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            content = {

                                items(
                                    count = images.size,
                                    itemContent = { index ->
                                        var showPlaceHolder by remember { mutableStateOf(true) }

                                        ImageItem(
                                            modifier = Modifier
                                                .padding(vertical = 8.dp)
                                                .placeholder(
                                                    visible = showPlaceHolder,
                                                    color = Color.LightGray,
                                                    shape = RoundedCornerShape(8.dp),
                                                    highlight = PlaceholderHighlight.fade(),
                                                ),
                                            link = images[index].url,
                                            onLoadingComplete = { showPlaceHolder = false }
                                        )
                                    }
                                )
                            },
                            contentPadding = PaddingValues(8.dp),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            )
        }
    )
}