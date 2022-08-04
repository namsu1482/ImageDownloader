package com.cns.imagedownloader.view.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cns.imagedownloader.model.SampleItem

class MainViewModel : ViewModel() {
    private val TAG = MainViewModel::class.simpleName
    var title: MutableLiveData<String> = MutableLiveData()
    var sampleItemList: MutableLiveData<ArrayList<SampleItem>> = MutableLiveData()

    private fun onPostTitle() {
        var tempTitle = title.value ?: ""
        title.postValue(tempTitle)
    }

    fun onClick() {
//        onPostTitle()
        initSampleList()

    }

    private fun initSampleList() {
        sampleItemList.value?.clear()
        var sampleList = sampleItemList.value ?: ArrayList()
        val range = (1..100)
        val arrayRange = (1..10)
        for (index: Int in 1..arrayRange.random()) {
            sampleList.add(SampleItem("${range.random()}", "${range.random()} Desc"))
        }
        sampleItemList.postValue(sampleList)

    }
}