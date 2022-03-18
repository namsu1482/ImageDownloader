package com.cns.imagedownloader.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// URL
const val BASE_URL = "https://pixabay.com/"

const val API_KEY = "25605493-c50d0c2814b5c6857e8fb1311"

class RetrofitHelper {
    lateinit var networkService: NetworkService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        networkService = retrofit.create(NetworkService::class.java)
    }
}



