package com.cns.imagedownloader.view.imgDetail

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cns.imagedownloader.R
import com.cns.imagedownloader.databinding.ActivityImgDetailBinding
import com.cns.imagedownloader.filedownloadmanager.FileDownloadManager
import com.cns.imagedownloader.model.HitsEntity
import com.cns.imagedownloader.notification.NotificationHelper
import com.cns.imagedownloader.view.main.MainActivity
import java.io.File
import java.io.FileOutputStream

class ImgDetailActivity : AppCompatActivity() {
    private val TAG = ImgDetailActivity::class.java.simpleName

    var fileName: String = ""
    lateinit var binding: ActivityImgDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imgData = intent.getSerializableExtra("img") as HitsEntity
        fileName = intent.getStringExtra("query").toString()
        performBinding(imgData)
    }

    private fun performBinding(imgData: HitsEntity) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_img_detail)
        binding.imgDetailAct = this
        binding.apply {
            progressVisibility = true
            binding.btnImgDownload.visibility = View.GONE
            Glide.with(this@ImgDetailActivity).load(imgData.largeImageURL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        showFailAlert()
                        progressVisibility = false
                        return true

                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        fileName = fileName + imgData.id
                        progressVisibility = false
                        binding.btnImgDownload.visibility = View.VISIBLE
                        return false
                    }
                })
                .into(imgFull)
        }
        binding.executePendingBindings()
    }

    private fun showFailAlert() {
        AlertDialog.Builder(this).apply {
            this.setTitle("이미지 표시 실패")
            this.setMessage("이미지 로딩에 실패하였습니다.")
            this.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                onBackPressed()
            })
        }
    }

    fun onClick() {
        FileDownloadManager(this).saveImage(binding.imgFull.drawable.toBitmap(), fileName)

        NotificationHelper(this).sendNotification()
        Toast.makeText(this, "이미지 다운로드가 완료되었습니다.", Toast.LENGTH_SHORT).show()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}