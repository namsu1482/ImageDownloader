package com.cns.imagedownloader.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.cns.imagedownloader.R
import com.cns.imagedownloader.databinding.ActivityMainBinding
import com.cns.imagedownloader.view.detail.DetailActivity
import com.cns.imagedownloader.view.search.SearchActivity

class MainActivity : AppCompatActivity() {
    private var mContext: Context = this
    lateinit var binding: ActivityMainBinding
    private var mainViewModel: MainViewModel = MainViewModel()
    private lateinit var adapter: SampleItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performBinding()
        observeData()
    }

    private fun performBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainAct = this
        binding.apply {
            mainviewmodel = mainViewModel
            lifecycleOwner = this@MainActivity

        }.run {
            executePendingBindings()
        }
        adapter = SampleItemAdapter()
        binding.recyclerViewItems.adapter = adapter

    }

    private fun observeData() {
        mainViewModel.sampleItemList.observe(this, Observer {
            adapter.apply {
                itemList = it
                notifyDataSetChanged()
                setOnItemClickListener {
                    Intent(
                        this@MainActivity,
                        SearchActivity::class.java
                    ).putExtra("item", it)
                        .run {
                            startActivity(this)
                        }

                }
            }
        })
    }

    fun onBtnClick() {
        Toast.makeText(this, "fab click", Toast.LENGTH_SHORT).show()
    }

}