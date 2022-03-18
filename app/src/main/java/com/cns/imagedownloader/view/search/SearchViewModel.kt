package com.cns.imagedownloader.view.search

import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.cns.imagedownloader.model.HitsEntity
import com.cns.imagedownloader.model.ImgItem
import com.cns.imagedownloader.model.SampleItem
import com.cns.imagedownloader.network.ImgRepository
import com.cns.imagedownloader.network.NetworkResult
import com.cns.imagedownloader.network.RetrofitHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel {
    private val TAG = SearchViewModel::class.simpleName
    var imgList: MutableLiveData<ArrayList<HitsEntity>> = MutableLiveData()
    var queryData: MutableLiveData<String> = MutableLiveData("")

    fun clearList() {
        imgList.value?.clear()
        imgList.postValue(ArrayList())

    }

    val submit = fun(query: String?) {
        queryData.value = query!!
        CoroutineScope(Dispatchers.Main).launch {
            Log.i(TAG, query)
            ImgRepository(RetrofitHelper().networkService).getSearchImgs(query).run {
                when (this) {
                    is NetworkResult.Success -> {
                        imgList.postValue(ArrayList(response?.hitsEntity))
                    }
                    is NetworkResult.Error -> {

                    }
                }
            }

        }
    }


}