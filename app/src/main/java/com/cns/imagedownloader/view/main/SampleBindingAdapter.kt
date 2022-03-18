package com.cns.imagedownloader.view.main

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cns.imagedownloader.model.SampleItem


object SampleBindingAdapter {
    //xml에 커스텀 속성 추가
    @BindingAdapter("items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, item: ArrayList<SampleItem>?) {
        if (recyclerView.adapter == null)
            recyclerView.adapter = SampleItemAdapter()

        val adapter = recyclerView.adapter as SampleItemAdapter

        adapter.itemList = item ?: ArrayList()
        adapter.notifyDataSetChanged()

    }

}