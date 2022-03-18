package com.cns.imagedownloader.network

import com.cns.imagedownloader.model.ImgItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("api/")
    suspend fun searchImg(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("image_type") imageType: String,
        @Query("page") page: Int,
    ): Response<ImgItem>
}