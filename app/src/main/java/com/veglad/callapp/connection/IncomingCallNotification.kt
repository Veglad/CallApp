package com.veglad.callapp.connection

import android.app.Notification
import android.app.NotificationManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import android.app.PendingIntent
import android.content.Intent
import com.veglad.callapp.IncomingCallActivity
import com.veglad.callapp.R
import timber.log.Timber


class IncomingCallNotification//createNotification(context)
    (context: Context) {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "INCOMING_CALL_NOTIFICATION_CHANNEL_ID"
        const val NOTIFICATION_ID = 128042
        const val NOTIFICATION_TAG = "INCOMING_CALL_NOTIFICATION_TAG"
        const val NOTIFICATION_CHANNEL_NAME = "INCOMING_CALL_NOTIFICATION"
    }

    init {
        if (Build.VERSION.SDK_INT < 26) {
            //createNotification(context)
        } else {
            createNotificationChannel(context)
        }
    }

    fun postIncomingCallNotification(context: Context, callee: String) {
        // Create an intent which triggers your fullscreen incoming call user interface.
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            flags = Intent.FLAG_ACTIVITY_NO_USER_ACTION or Intent.FLAG_ACTIVITY_NEW_TASK
            setClass(context, IncomingCallActivity::class.java)
        }
        val pendingIntent = PendingIntent.getActivity(context, 1, intent, 0)
        val builder = initNotificationBuilder(pendingIntent, context, callee)

        // Set notification as insistent to cause your ringtone to loop.
        val notification = builder.build()
        notification.flags = notification.flags or Notification.FLAG_INSISTENT

        // TODO: Use builder.addAction(..) to add buttons to answer or reject the call.

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        Timber.tag("com.veglad.callapp").d("postIncomingCallNotification")
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {
        val channel = initChannel()

        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        channel.setSound(
            ringtoneUri, AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )

        val mgr = context.getSystemService(NotificationManager::class.java)
        mgr!!.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initChannel() : NotificationChannel {
        return NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
    }

    private fun initNotificationBuilder(
        pendingIntent: PendingIntent,
        context: Context,
        callee: String
    ) : Notification.Builder {
        // Build the notification as an ongoing high priority item; this ensures it will show as
        // a heads up notification which slides down over top of the current content.
        val builder = if (Build.VERSION.SDK_INT < 26) {
            Notification.Builder(context).apply {
                setPriority(Notification.PRIORITY_HIGH)
            }
        } else {
            Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
        }
        builder.setOngoing(true)

        // Set notification content intent to take user to fullscreen UI if user taps on the
        // notification body.
        builder.setContentIntent(pendingIntent)
        // Set full screen intent to trigger display of the fullscreen UI when the notification
        // manager deems it appropriate.
        builder.setFullScreenIntent(pendingIntent, true)

        // Setup notification content.
        builder.setSmallIcon(R.drawable.ic_call)
        val titleTemplate = context.getString(R.string.incoming_call_notification_title_template)
        val title = String.format(titleTemplate, callee)
        builder.setContentTitle(title)

        return builder
    }
}