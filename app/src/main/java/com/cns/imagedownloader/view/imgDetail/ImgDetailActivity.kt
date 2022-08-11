package com.cns.imagedownloader.view.imgDetail

import android.content.DialogInterface
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cns.imagedownloader.R
import com.cns.imagedownloader.databinding.ActivityImgDetailBinding
import com.cns.imagedownloader.imgfileManger.ImgFileManager
import com.cns.imagedownloader.model.ImageItemInfo
import com.cns.imagedownloader.notification.NotificationHelper

class ImgDetailActivity : AppCompatActivity() {
    private val TAG = ImgDetailActivity::class.java.simpleName

    var fileName: String = ""
    lateinit var binding: ActivityImgDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imgData = intent.getSerializableExtra("img") as ImageItemInfo
        fileName = intent.getStringExtra("query").toString()
        performBinding(imgData)
    }

    private fun performBinding(imgData: ImageItemInfo) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_img_detail)
        binding.imgDetailAct = this
        binding.apply {
            progressVisibility = true
            binding.btnImgDownload.visibility = View.GONE
            Glide.with(this@ImgDetailActivity).load(imgData.mainUrl)
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
                        fileName = fileName + imgData.imgId
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
        ImgFileManager(this).saveImage(binding.imgFull.drawable.toBitmap(), fileName)

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