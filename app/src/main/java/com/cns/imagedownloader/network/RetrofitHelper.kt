package com.cns.imagedownloader.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// URL
const val BASE_URL = "https://pixabay.com/"

const val PIXABAY_API_KEY = "25605493-c50d0c2814b5c6857e8fb1311"

const val KAKAO_SEARCH_URL = "https://dapi.kakao.com/v2/search/"

class RetrofitHelper(searchType: SEARCH_TYPE = SEARCH_TYPE.PIXABAY) {
    var networkService: NetworkService

//    init {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        networkService = retrofit.create(NetworkService::class.java)
//    }

    init {
        var url = BASE_URL
        if (searchType == SEARCH_TYPE.KAKAO) {
            url = KAKAO_SEARCH_URL
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        networkService = retrofit.create(NetworkService::class.java)

    }

    enum class SEARCH_TYPE {
        PIXABAY,
        KAKAO
    }
}



