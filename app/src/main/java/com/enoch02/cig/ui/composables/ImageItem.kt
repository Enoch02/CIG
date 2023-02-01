package com.enoch02.cig.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.enoch02.cig.data.models.MainViewModel

@Composable
fun ImageItem(
    modifier: Modifier,
    link: String,
    onLoadingComplete: () -> Unit,
    mainViewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(link)
                .crossfade(true)
                .build(),
            //alignment = Alignment,
            contentDescription = null,
            onSuccess = { onLoadingComplete.invoke() }
        )
        IconButton(
            onClick = { mainViewModel.downloadImage(context, link) },
            content = {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download"
                )
            },
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}