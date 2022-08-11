package com.cns.imagedownloader.network

import com.cns.imagedownloader.model.kakaoitem.KakaoImgList
import com.cns.imagedownloader.model.pixabayitem.PixabayImgItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NetworkService {
    @GET("api/")
    suspend fun searchImg(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("image_type") imageType: String,
        @Query("page") page: Int,
    ): Response<PixabayImgItem>

    @GET("image")
    suspend fun searchKakaoImg(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("sort") accuracy: String = "accuracy",
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 80
    ):Response<KakaoImgList>
}