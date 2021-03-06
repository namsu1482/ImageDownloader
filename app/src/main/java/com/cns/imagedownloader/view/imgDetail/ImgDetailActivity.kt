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
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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
import com.cns.imagedownloader.model.HitsEntity
import com.cns.imagedownloader.view.main.MainActivity
import java.io.FileOutputStream

class ImgDetailActivity : AppCompatActivity() {
    private val TAG = ImgDetailActivity::class.java.simpleName
    var fileName: String = ""
    lateinit var binding: ActivityImgDetailBinding
    val NOTIFICATION_ID = 101
    val CHANNEL_ID = "imageDownloader"

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
            this.setTitle("????????? ?????? ??????")
            this.setMessage("????????? ????????? ?????????????????????.")
            this.setPositiveButton("??????", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                onBackPressed()
            })
        }
    }

    fun onClick() {
        saveImg()
        sendNotification()
        Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show()

    }

    private fun saveImg() {
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

    // notification bar ??? ???????????? progress ??????
    private fun sendNotification() {
        createNotificationChannel()

        val mainIntent = Intent(this, MainActivity::class.java).apply {
            this.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            this.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val pendingIntent = let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                android.app.PendingIntent.getActivities(
                    this, 0,
                    kotlin.arrayOf(mainIntent), android.app.PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                android.app.PendingIntent.getActivities(
                    this, 0,
                    kotlin.arrayOf(mainIntent), android.app.PendingIntent.FLAG_ONE_SHOT
                )
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationBuilder = Notification.Builder(this, CHANNEL_ID).apply {
                this.setSmallIcon(R.drawable.ic_launcher_foreground)
                this.setContentTitle(getString(R.string.app_name))
                this.setContentText("????????? ????????????")
                // notification ????????? notification ?????? ?????? ??????
                this.setAutoCancel(true)
                // notification ????????? ????????? ????????? ??????
                this.setContentIntent(pendingIntent)

            }

            NotificationManagerCompat.from(this@ImgDetailActivity).run {
                this.notify(NOTIFICATION_ID, notificationBuilder.build())
            }
        } else {
            return
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationName = getString(R.string.app_name)
            val description = "????????? ????????????"
            val notificationImportance = NotificationManager.IMPORTANCE_DEFAULT
            //notification ?????? ??????
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, notificationName, notificationImportance).apply {
                    this.description = description
                }

            // notification Manager??? ?????? ??????
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).apply {
                this.createNotificationChannel(notificationChannel)
            }
        }
    }
}