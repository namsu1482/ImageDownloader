package com.cns.imagedownloader.network

import android.util.Log
import com.cns.imagedownloader.BuildConfig
import com.cns.imagedownloader.model.kakaoitem.KakaoImgList
import com.cns.imagedownloader.model.pixabayitem.PixabayImgItem
import com.cns.imagedownloader.model.pixabayitem.ListData
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

class ImgRepository(private val service: NetworkService) {
    private val TAG = ImgRepository::class.simpleName
    suspend fun getSearchImgs(query: String): NetworkResult<PixabayImgItem> = withContext(Dispatchers.IO) {
        callResponse {
            service.searchImg(BuildConfig.PIXABAY_API_KEY, query, "photo", 1)
        }

    }

    suspend fun getKakaoImgs(query: String):NetworkResult<KakaoImgList> = withContext(Dispatchers.IO){
        callResponse {
            val apiKey = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
            service.searchKakaoImg(apiKey, query)
        }
    }



    private fun parseError(body: ResponseBody): NetworkResult.Error {
        val element = JsonParser().parse(body.string()).asJsonObject
        val code = if (element.has("code")) {
            element.get("code").asString
        } else {
            null
        }
        val errMsg = if (element.has("message")) {
            element.get("message").asString
        } else {
            null
        }

        return NetworkResult.Error(code, errMsg)

    }

    private suspend fun <T : Any> callResponse(call: suspend () -> Response<T>): NetworkResult<T> {
        val response = call.invoke()
        if (response.isSuccessful) {
            Log.i(TAG, "response : ${response.body().toString()}")
            return NetworkResult.Success(response.body())

        } else {
            response.errorBody()?.let {
                return parseError(it)

            } ?: kotlin.run {
                return NetworkResult.Error(null, null)
            }

        }
    }

    private suspend fun <T : Any> callArray(call: suspend () -> Response<List<T>>): List<T> {
        val response = call.invoke()
        return response.body() ?: emptyList()
    }

    private suspend fun <T : Any> callList(call: suspend () -> Response<ListData<T>>): ListData<T> {
        val response = call.invoke()
        return response.body() ?: ListData()

    }

}