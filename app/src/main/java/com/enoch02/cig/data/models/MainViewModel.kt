package com.enoch02.cig.data.models

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.cig.data.ApiService
import kotlinx.coroutines.launch
import java.io.File

const val TAG = "MAIN_VIEW_MODEL"

class MainViewModel : ViewModel() {
    private val apiService = ApiService.getInstance()
    private var generatedImage: List<Image> by mutableStateOf(emptyList())
    private var apiKey by mutableStateOf("")

    var showResultDialog by mutableStateOf(false)
    var currentLoadingState by mutableStateOf(State.NOT_LOADING)
    var prompt by mutableStateOf("")
    var amount by mutableStateOf(1f)
    var selectedChip by mutableStateOf(0)

    fun generateImage(request: RequestBody) {
        currentLoadingState = State.LOADING
        viewModelScope.launch {
            try {
                val headers =
                    mapOf("Content-Type" to "application/json", "Authorization" to "Bearer $apiKey")
                generatedImage = apiService.generateImages(
                    headers = headers,
                    request = request
                ).data
                Log.d(TAG, "generateImage: ${generatedImage[0].url}")
                if (generatedImage.isNotEmpty()) {
                    showResultDialog = true
                    currentLoadingState = State.NOT_LOADING
                }
            } catch (e: Exception) {
                Log.e(TAG, "generateImage: ${e.message}", e.cause)
                currentLoadingState = State.FAILED
            }
        }
    }

    fun getGeneratedImages() = generatedImage

    fun reset() {
        prompt = ""
        amount = 1f
        selectedChip = 0
        generatedImage = emptyList()
    }

    fun setAPIkey(key: String) {
        apiKey = key
    }

    fun getAPIkey() = apiKey

    fun downloadImage(context: Context, url: String) {
        val uri = Uri.parse(url)
        val fileName = url.substring(url.lastIndexOf("/img") + 1, url.lastIndexOf(".png") + 4)
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            fileName
        )

        if (file.exists()) {
            Log.d(TAG, "downloadImage: A file with the name '$fileName' exists!")
            return
        } else {
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(uri)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(fileName)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setMimeType("image/jpg")
                .setAllowedOverMetered(true)

            downloadManager.enqueue(request)
        }
    }

    fun downloadAll(context: Context) {
        generatedImage.forEach { image ->
            downloadImage(context, image.url)
        }
    }
}

enum class State {
    LOADING, FAILED, NOT_LOADING
}
