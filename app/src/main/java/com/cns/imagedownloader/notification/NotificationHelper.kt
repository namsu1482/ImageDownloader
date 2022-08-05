package com.cns.imagedownloader.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.cns.imagedownloader.R
import com.cns.imagedownloader.view.main.MainActivity

class NotificationHelper(val context: Context) {
    val NOTIFICATION_ID = 101
    private val CHANNEL_ID = "imageDownloader"

    fun sendNotification() {
        val notificationBuilder: Notification.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            notificationBuilder = Notification.Builder(context, CHANNEL_ID)
        } else {
            notificationBuilder = Notification.Builder(context)
        }

        val mainIntent = Intent(context, MainActivity::class.java).apply {
            this.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            this.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val pendingIntent = let {
            //android 12 부터  pendingIntent object 변경 가능 여부 Flag 설정 필요
            var intentFlag = android.app.PendingIntent.FLAG_ONE_SHOT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                intentFlag = android.app.PendingIntent.FLAG_IMMUTABLE

            }
            android.app.PendingIntent.getActivities(
                context, 0,
                arrayOf(mainIntent), intentFlag
            )
        }

        notificationBuilder.apply {
            this.setSmallIcon(R.drawable.ic_launcher_foreground)
            this.setContentTitle(context.getString(R.string.app_name))
            this.setContentText("이미지 다운로드가 완료되었습니다.")
            // notification 클릭시 notification 자동 삭제 여부
            this.setAutoCancel(true)
            // notification 클릭시 실행할 인텐트 설정
            this.setContentIntent(pendingIntent)
        }

        NotificationManagerCompat.from(context).run {
            this.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationName = context.getString(R.string.app_name)
        val description = "이미지 다운로드"
        val notificationImportance = NotificationManager.IMPORTANCE_DEFAULT
        //notification 채널 생성
        val notificationChannel =
            NotificationChannel(CHANNEL_ID, notificationName, notificationImportance).apply {
                this.description = description
            }

        // notification Manager에 채널 등록
        (context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).apply {
            this.createNotificationChannel(notificationChannel)
        }
    }
}