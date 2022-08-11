package com.cns.imagedownloader.view.search

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.cns.imagedownloader.R
import com.cns.imagedownloader.databinding.ActivitySearchBinding
import com.cns.imagedownloader.model.ImageItemInfo
import com.cns.imagedownloader.view.imgDetail.ImgDetailActivity

class SearchActivity : AppCompatActivity() {
    private val TAG = SearchActivity::class.simpleName
    lateinit var binding: ActivitySearchBinding
    var searchViewModel = SearchViewModel()
    lateinit var imgListAdapter: ImgListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.apply {
            searchVm = searchViewModel
            lifecycleOwner = this@SearchActivity

            imgListAdapter = ImgListAdapter()
            recyclerviewImg.adapter = imgListAdapter
            recyclerviewImg.layoutManager = GridLayoutManager(this@SearchActivity, 3)

            binding.layoutSwipeRefresh.setOnRefreshListener {
                searchViewModel.clearList()
                searchViewModel.submit(searchViewModel.queryData.value)
                binding.tvNoResult.visibility = View.GONE
//                searchViewModel.getImgList(searchViewModel.queryData.value ?: "")
                layoutSwipeRefresh.isRefreshing = false
            }

        }.run {
            executePendingBindings()
            binding.tvNoResult.visibility = View.GONE
        }

        searchViewModel.imgList.observe(this, Observer {
            imgListAdapter.apply {
                imgList = it as ArrayList<ImageItemInfo>

                notifyDataSetChanged()
                if (imgList.isEmpty()) {
                    if (searchViewModel.queryData.value.isNullOrEmpty()) {
                        binding.tvNoResult.visibility = View.VISIBLE

                    } else {
                        binding.tvNoResult.visibility = View.GONE
                    }
                } else {
                    binding.tvNoResult.visibility = View.GONE
                }
                setItemClickListener {
                    Intent(this@SearchActivity, ImgDetailActivity::class.java).apply {
                        putExtra("img", it)
                        putExtra("query", searchViewModel.queryData.value)
                    }.run {
                        startActivity(this)
                    }
                }
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}