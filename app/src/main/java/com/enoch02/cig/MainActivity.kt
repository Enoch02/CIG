@file:OptIn(ExperimentalMaterial3Api::class)

package com.enoch02.cig

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enoch02.cig.data.models.MainViewModel
import com.enoch02.cig.data.models.State
import com.enoch02.cig.ui.screens.HomeScreen
import com.enoch02.cig.ui.screens.ResultDialog
import com.enoch02.cig.ui.theme.CIGTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CIGTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = stringResource(R.string.app_name))
                            }
                        )
                    },
                    content = { innerPadding ->
                        val mainViewModel: MainViewModel = viewModel()
                        var showDialog by rememberSaveable {
                            mutableStateOf(mainViewModel.getAPIkey().isBlank())
                        }
                        val context = LocalContext.current

                        if (showDialog) {
                            var apiKey by rememberSaveable { mutableStateOf("") }

                            AlertDialog(
                                onDismissRequest = { },
                                title = {
                                    Text(text = "Enter your OpenAI api key")
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            if (apiKey.isNotBlank()) {
                                                mainViewModel.setAPIkey(apiKey)
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Type something...",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            showDialog = false
                                        },
                                        content = {
                                            Text(text = "Continue")
                                        }
                                    )
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = { finish() },
                                        content = {
                                            Text(text = "Exit")
                                        }
                                    )
                                },
                                text = {
                                    Column {
                                        OutlinedTextField(
                                            value = apiKey,
                                            onValueChange = { apiKey = it },
                                        )
                                        Text(
                                            text = "Don't have one? tap me",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    context.startActivity(
                                                        Intent(
                                                            Intent.ACTION_VIEW,
                                                            Uri.parse("https://beta.openai.com/signup")
                                                        )
                                                    )
                                                }
                                        )
                                    }
                                }
                            )
                        }

                        if (mainViewModel.currentLoadingState == State.LOADING) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(innerPadding)
                            )
                        }

                        HomeScreen(modifier = Modifier.padding(innerPadding))

                        if (mainViewModel.showResultDialog) {
                            ResultDialog()
                        }
                    },
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}
