package com.cns.imagedownloader.view.main

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cns.imagedownloader.model.SampleItem

class MainViewModel : ViewModel() {
    private val TAG = MainViewModel::class.simpleName
    var title: MutableLiveData<String> = MutableLiveData()
    var sampleItemList: MutableLiveData<ArrayList<SampleItem>> = MutableLiveData()

    fun onPostTitle() {
        var tempTitle = title.value ?: "Default"
        title.postValue(tempTitle)
    }


    fun onClick() {
        onPostTitle()
        initSampleList()
    }

    fun initSampleList() {
        var sampleList = sampleItemList.value ?: ArrayList()
        val range = (1..100)
        sampleList = arrayListOf(

            SampleItem("${range.random()}", "${range.random()} Desc"),
            SampleItem("${range.random()}", "${range.random()} Desc")
        )
        sampleItemList.postValue(sampleList)

    }
}