package com.cns.imagedownloader.view.search

import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter

object searchListBinding {
    @BindingAdapter("onSearchViewListener")
    @JvmStatic
    fun setOnSearchViewListener(searchView: SearchView, postSubmit:(String?)->Unit){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                postSubmit(query)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
}