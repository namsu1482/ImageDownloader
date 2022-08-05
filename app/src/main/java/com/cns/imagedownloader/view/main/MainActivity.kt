package com.cns.imagedownloader.view.main

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.cns.imagedownloader.R
import com.cns.imagedownloader.databinding.ActivityMainBinding
import com.cns.imagedownloader.view.detail.DetailActivity
import com.cns.imagedownloader.view.search.SearchActivity
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private var mContext: Context = this
    lateinit var binding: ActivityMainBinding
    private var mainViewModel: MainViewModel = MainViewModel()
    private lateinit var adapter: SampleItemAdapter

    private val permissionReqCode = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
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


    fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val permissionList: Array<String> = arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

            ActivityCompat.requestPermissions(this, permissionList, permissionReqCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {

        } else {
            AlertDialog.Builder(this).setMessage("이미지 다운로드가 불가능 합니다.")
                .setPositiveButton("Ok", { v, event ->
                    finish()
                })
        }

    }
}