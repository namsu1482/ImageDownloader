package com.cns.imagedownloader.view.imgDetail

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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
import com.cns.imagedownloader.model.HitsEntity
import com.cns.imagedownloader.view.main.MainActivity
import java.io.FileOutputStream

class ImgDetailActivity : AppCompatActivity() {
    private val TAG = ImgDetailActivity::class.java.simpleName
    val filePath: String = "imageDownloader"
    var fileName: String = ""
    lateinit var binding: ActivityImgDetailBinding
    val NOTIFICATION_ID = 101
    var CHANNEL_ID = "imageDownloader"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imgData = intent.getSerializableExtra("img") as HitsEntity
        fileName = intent.getStringExtra("query").toString()
        performBinding(imgData)
    }

    fun performBinding(imgData: HitsEntity) {
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

    fun onClick() {
        saveImg()
        sendNotification()
        Toast.makeText(this, "다운로드", Toast.LENGTH_SHORT).show()

    }

    fun saveImg() {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName + ".jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }

        val item = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

        contentResolver.openFileDescriptor(item, "w").use {
            val imgBitmap = binding.imgFull.drawable.toBitmap()
            FileOutputStream(it!!.fileDescriptor).use {
                imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                it.close()

                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(item, values, null, null)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    // notification bar 에 다운로드 progress 표시
    fun sendNotification() {
        createNotification()

        val mainIntent = Intent(this, MainActivity::class.java).apply {
            this.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            this.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val pendingIntent =
            PendingIntent.getActivities(this, 0, arrayOf(mainIntent), PendingIntent.FLAG_ONE_SHOT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationBuilder = Notification.Builder(this, CHANNEL_ID).apply {
                this.setSmallIcon(R.drawable.ic_launcher_foreground)
                this.setContentTitle(getString(R.string.app_name))
                this.setContentText("이미지 다운로드")
                // notification 클릭시 notification 자동 삭제 여부
                this.setAutoCancel(true)
                // notification 클릭시 실행할 인텐트 설정
                this.setContentIntent(pendingIntent)

            }

            NotificationManagerCompat.from(this@ImgDetailActivity).run {
                this.notify(NOTIFICATION_ID, notificationBuilder.build())
            }
        } else {
            return
        }

    }

    fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationName = getString(R.string.app_name)
            val description = "이미지 다운로드"
            val notificationImportance = NotificationManager.IMPORTANCE_DEFAULT
            //notification 채널 생성
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, notificationName, notificationImportance).apply {
                    this.description = description
                }

            // notification Manager에 채널 등록
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).apply {
                this.createNotificationChannel(notificationChannel)
            }
        }
    }
}