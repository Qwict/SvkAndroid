package com.qwict.svkandroid.tasks

import android.app.Notification.DEFAULT_ALL
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color.RED
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.media.RingtoneManager.getDefaultUri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import com.qwict.svkandroid.MainActivity
import com.qwict.svkandroid.R

const val NOTIFICATION_ID = "appName_notification_id"
const val NOTIFICATION_NAME = "appName"
const val NOTIFICATION_CHANNEL = "appName_channel_01"
// const val NOTIFICATION_WORK = "appName_notification_work"

fun makeStatusNotification(id: Int, title: String, message: String, context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
    intent.putExtra("NOTIFICATION_ID", id)

    val notificationManager =
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    val notification = NotificationCompat.Builder(context, NOTIFICATION_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title).setContentText(message)
        .setDefaults(DEFAULT_ALL)

    notification.priority = PRIORITY_MAX

    if (SDK_INT >= O) {
        notification.setChannelId(NOTIFICATION_CHANNEL)

        val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
            .setContentType(CONTENT_TYPE_SONIFICATION).build()

        val channel =
            NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, IMPORTANCE_HIGH)

        channel.enableLights(true)
        channel.lightColor = RED
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100, 200, 300)
        channel.setSound(ringtoneManager, audioAttributes)
        notificationManager.createNotificationChannel(channel)
    }

    notificationManager.notify(id, notification.build())
}
