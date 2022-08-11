package com.cns.imagedownloader.view.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cns.imagedownloader.model.ImageItemInfo
import com.cns.imagedownloader.network.ImgRepository
import com.cns.imagedownloader.network.NetworkResult
import com.cns.imagedownloader.network.RetrofitHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class SearchViewModel {
    private val TAG = SearchViewModel::class.simpleName
    var imgList: MutableLiveData<ArrayList<ImageItemInfo>> = MutableLiveData()
    var queryData: MutableLiveData<String> = MutableLiveData("")

    fun clearList() {
        imgList.value?.clear()
        imgList.postValue(ArrayList())

    }

    val submit = fun(query: String?) {
        queryData.value = query!!

        CoroutineScope(Dispatchers.Main).launch {
            Log.i(TAG, query)

            //pixabay
            ImgRepository(RetrofitHelper().networkService).getSearchImgs(query).run {
                when (this) {
                    is NetworkResult.Success -> {

                        imgList.postValue(ArrayList(response?.hitsEntity))
                    }
                    is NetworkResult.Error -> {

                    }
                }
            }

            //Kakao
//            ImgRepository(RetrofitHelper(RetrofitHelper.SEARCH_TYPE.KAKAO).networkService).getKakaoImgs(
//                query
//            ).run {
//                when (this) {
//                    is NetworkResult.Success -> {
//                        imgList.postValue(ArrayList(response?.kakaoItemList))
//                    }
//                    is NetworkResult.Error -> {
//
//                    }
//                }
//            }

        }
    }


}