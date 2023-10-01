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
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NotificationScheduler @Inject constructor(@ApplicationContext private val context: Context) {
    fun scheduleNotification(task: Task) {
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

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            getNotificationTimeInMillis(),
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun getNotificationTimeInMillis(): Long {
        // Set the time to 12:00 PM
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 12)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.timeInMillis
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