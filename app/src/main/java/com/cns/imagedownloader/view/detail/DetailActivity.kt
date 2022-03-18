package com.cns.imagedownloader.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cns.imagedownloader.R
import com.cns.imagedownloader.databinding.ActivityDetailBinding
import com.cns.imagedownloader.model.SampleItem

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var sampleItem: SampleItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        sampleItem = intent.getSerializableExtra("item") as SampleItem

        initData()

    }

    private fun initData() {
        binding.tvTitle.text = sampleItem.title
        binding.tvDesc.text = sampleItem.desc
    }


}