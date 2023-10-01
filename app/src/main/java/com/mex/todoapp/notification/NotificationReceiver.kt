package com.mex.todoapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.mex.todoapp.notification.NotificationConstants.TASK_CHANNEL_ID
import com.mex.todoapp.notification.NotificationConstants.TASK_CHANNEL_NAME
import com.mex.todoapp.notification.NotificationConstants.TASK_DESCRIPTION
import com.mex.todoapp.notification.NotificationConstants.TASK_ID
import com.mex.todoapp.notification.NotificationConstants.TASK_TITLE

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val taskId = intent?.getIntExtra(TASK_ID, 0) ?: 0
        val taskTitle = intent?.getStringExtra(TASK_TITLE)
        val taskDescription = intent?.getStringExtra(TASK_DESCRIPTION)


        if (taskTitle != null && taskDescription != null) {
            showNotification(context!!, taskId, taskTitle, taskDescription, TASK_CHANNEL_ID)
        }
    }

    private fun showNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        channelId: String
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, TASK_CHANNEL_NAME, importance)
        notificationManager.createNotificationChannel(channel)

        // Build the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Show the notification
        notificationManager.notify(id, notification)
    }
}