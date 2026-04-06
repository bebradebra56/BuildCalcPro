package com.buildcal.probuild.rgjof.presentation.notificiation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import com.buildcal.probuild.BuildCalcProActivity
import com.buildcal.probuild.R
import com.buildcal.probuild.rgjof.presentation.app.BuildCalcProApplication
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val BUILD_CALC_PRO_CHANNEL_ID = "build_calc_pro_notifications"
private const val BUILD_CALC_PRO_CHANNEL_NAME = "BuildCalcPro Notifications"
private const val BUILD_CALC_PRO_NOT_TAG = "BuildCalcPro"

class BuildCalcProPushService : FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Обработка notification payload
        remoteMessage.notification?.let {
            if (remoteMessage.data.contains("url")) {
                buildCalcProShowNotification(it.title ?: BUILD_CALC_PRO_NOT_TAG, it.body ?: "", data = remoteMessage.data["url"])
            } else {
                buildCalcProShowNotification(it.title ?: BUILD_CALC_PRO_NOT_TAG, it.body ?: "", data = null)
            }
        }

        // Обработка data payload
        if (remoteMessage.data.isNotEmpty()) {
            buildCalcProHandleDataPayload(remoteMessage.data)
        }
    }

    private fun buildCalcProShowNotification(title: String, message: String, data: String?) {
        val buildCalcProNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Создаем канал уведомлений для Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                BUILD_CALC_PRO_CHANNEL_ID,
                BUILD_CALC_PRO_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            buildCalcProNotificationManager.createNotificationChannel(channel)
        }

        val buildCalcProIntent = Intent(this, BuildCalcProActivity::class.java).apply {
            putExtras(bundleOf(
                "url" to data
            ))
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val buildCalcProPendingIntent = PendingIntent.getActivity(
            this,
            0,
            buildCalcProIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val buildCalcProNotification = NotificationCompat.Builder(this, BUILD_CALC_PRO_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.build_calc_pro_noti_ic)
            .setAutoCancel(true)
            .setContentIntent(buildCalcProPendingIntent)
            .build()

        buildCalcProNotificationManager.notify(System.currentTimeMillis().toInt(), buildCalcProNotification)
    }

    private fun buildCalcProHandleDataPayload(data: Map<String, String>) {
        data.forEach { (key, value) ->
            Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "Data key=$key value=$value")
        }
    }
}