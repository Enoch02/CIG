package com.enoch02.cig.data

import com.enoch02.cig.data.models.AIResponse
import com.enoch02.cig.data.models.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST

const val API_KEY =
    "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"  //TODO: ask for api key when the app starts

interface ApiService {

    @POST("images/generations")
    //@Headers("Content-Type: application/json"/*, "Authorization: Bearer $API_KEY"*/)
    suspend fun generateImages(
        //@Header("key") key: String,
        @HeaderMap headers: Map<String, String>,
        @Body request: RequestBody
    ): AIResponse

    companion object {
        var apiService: ApiService? = null

        fun getInstance(): ApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl("https://api.openai.com/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}