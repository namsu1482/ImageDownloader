package com.cns.imagedownloader.network

sealed class NetworkResult<out T:Any> {
    class Success<out T:Any>(val response: T?) : NetworkResult<T>()
    class Error(val errCode: String?, val errMsg: String?) : NetworkResult<Nothing>()
}