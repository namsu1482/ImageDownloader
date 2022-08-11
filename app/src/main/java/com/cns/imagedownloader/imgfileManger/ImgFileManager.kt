package com.cns.imagedownloader.imgfileManger

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImgFileManager(var context: Context) {
    private val TAG = ImgFileManager::class.java.simpleName
    val DIR_NAME = "ImageDownloader"

    fun saveImage(bitmap: Bitmap, fileName: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return saveImg(bitmap, fileName)

        } else {
            return saveImgLowVersion(bitmap, fileName)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImg(bitmap: Bitmap, fileName: String): Boolean {
        val values = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "${fileName}.jpg"
            )
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            // 디렉토리 설정
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/${DIR_NAME}")

        }
        try {
            val item =
                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )!!

            context.contentResolver.openFileDescriptor(item, "w").use {
                val imgBitmap = bitmap
                FileOutputStream(it!!.fileDescriptor).use {
                    imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    it.close()

                    values.put(MediaStore.Images.Media.IS_PENDING, 0)
                    context.contentResolver.update(item, values, null, null)

                }
            }
        } catch (e: IOException) {
            Log.e(TAG, e.message.toString())
            return false
        }
        return true
    }

    private fun saveImgLowVersion(bitmap: Bitmap, fileName: String): Boolean {
        val imgDir =
            File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}${File.separator}${DIR_NAME}")
        Log.i(TAG, "${imgDir.path}")
        if (!imgDir.exists()) {
            if (!imgDir.mkdirs()) {
                Log.e(TAG, "mkDirs Error")
                return false
            }
        }

        val imgFile = File(imgDir, "${fileName}.jpg")
        val imgBitmap = bitmap

        try {
            FileOutputStream(imgFile).apply {
                imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
                this.close()
            }

            //MediaStore에 데이터 저장 -> 파일 처리만 하면 gallery 에는 반영이 안된다.
            val values = ContentValues().apply {
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    "${fileName}.jpg"
                )
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                put(MediaStore.Images.Media.DATA, imgFile.path)

            }
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        } catch (e: IOException) {
            Log.i(TAG, e.message.toString())
            return false
        }
        return true
    }

    fun getImg(): Bitmap? {
        var imgResult: Bitmap? = null
        val intent = Intent().apply {
            this.setType("image/*")
            this.setAction(Intent.ACTION_GET_CONTENT)

        }

        val lauchActivity =
            (context as AppCompatActivity).registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val resultData = result.data
                    val imgUri = resultData?.data ?: return@registerForActivityResult
                    imgResult = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val imgSrc = ImageDecoder.createSource(context.contentResolver, imgUri)
                        val imgBitmap = ImageDecoder.decodeBitmap(imgSrc)
                        imgBitmap

                    } else {
                        val img = MediaStore.Images.Media.getBitmap(context.contentResolver, imgUri)
                        img
                    }

                }
            }


            lauchActivity.launch(intent)

        return imgResult

    }
}