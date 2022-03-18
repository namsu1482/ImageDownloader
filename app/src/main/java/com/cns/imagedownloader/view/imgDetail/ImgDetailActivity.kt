package com.cns.imagedownloader.view.imgDetail

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.ProgressBarBindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cns.imagedownloader.R
import com.cns.imagedownloader.databinding.ActivityImgDetailBinding
import com.cns.imagedownloader.model.HitsEntity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ImgDetailActivity : AppCompatActivity() {
    private val TAG = ImgDetailActivity::class.java.simpleName
    val filePath: String = "imageDownloader"
    var fileName: String = ""
    lateinit var binding: ActivityImgDetailBinding

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
                        return false
                    }
                })
                .into(imgFull)
        }
        binding.executePendingBindings()
    }

    fun onClick() {
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
        Toast.makeText(this, "다운로드", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}