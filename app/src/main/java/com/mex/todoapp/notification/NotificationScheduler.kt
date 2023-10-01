package com.mex.todoapp.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.mex.todoapp.data.model.Task
import com.mex.todoapp.notification.NotificationConstants.TASK_DESCRIPTION
import com.mex.todoapp.notification.NotificationConstants.TASK_ID
import com.mex.todoapp.notification.NotificationConstants.TASK_TITLE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NotificationScheduler @Inject constructor(@ApplicationContext private val context: Context) {
    fun scheduleNotification(task: Task, notificationTimeMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(TASK_ID, task.title)
        intent.putExtra(TASK_TITLE, task.title)
        intent.putExtra(TASK_DESCRIPTION, task.description)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            notificationTimeMillis,
            pendingIntent
        )
    }

    fun cancelScheduledNotification(notificationId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}

object NotificationConstants {
    const val TASK_ID = "taskId"
    const val TASK_TITLE = "taskTitle"
    const val TASK_DESCRIPTION = "taskDescription"

    const val TASK_CHANNEL_ID = "TASK_CHANNEL_ID"
    const val TASK_CHANNEL_NAME = "TASK_CHANNEL_NAME"
}