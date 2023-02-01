package com.enoch02.cig.data.models

data class Image(val url: String)

data class AIResponse(val created: String, val data: List<Image>)

data class RequestBody(
    val prompt: String,
    val n: Int,
    val size: String
)