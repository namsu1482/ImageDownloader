package com.cns.imagedownloader.view.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.cns.imagedownloader.R
import com.cns.imagedownloader.databinding.ActivityMainBinding
import com.cns.imagedownloader.view.search.SearchActivity

class MainActivity : AppCompatActivity() {
    private var mContext: Context = this
    lateinit var binding: ActivityMainBinding
    private var mainViewModel: MainViewModel = MainViewModel()
    private lateinit var adapter: SampleItemAdapter

    private val permissionReqCode = 10

    var imgResult: Bitmap? = null
    var lauchActivityLauncher: ActivityResultLauncher<Intent>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        performBinding()
        observeData()

        //lifecycle 주의 -> lifecycleOwner가 start 되기전에(onCreate,onStart class init등) 초기화 진행 해야함
        this.lauchActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val resultData = result.data
                    val imgUri = resultData?.data ?: return@registerForActivityResult
                    imgResult = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val imgSrc = ImageDecoder.createSource(contentResolver, imgUri)
                        val imgBitmap = ImageDecoder.decodeBitmap(imgSrc)
                        imgBitmap

                    } else {
                        val img = MediaStore.Images.Media.getBitmap(contentResolver, imgUri)
                        img
                    }

                }
            }


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
        val intent = Intent().apply {
            this.setType("image/*")
            this.setAction(Intent.ACTION_GET_CONTENT)

        }
        lauchActivityLauncher?.launch(intent)
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