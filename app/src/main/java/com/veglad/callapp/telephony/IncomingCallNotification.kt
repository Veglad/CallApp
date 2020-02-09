package com.veglad.callapp.telephony

import android.app.Notification
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import android.app.PendingIntent
import android.content.Intent
import com.veglad.callapp.view.IncomingCallActivity
import com.veglad.callapp.R
import com.veglad.callapp.helpers.IntentFactory
import timber.log.Timber

class IncomingCallNotification
    (context: Context) {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "INCOMING_CALL_NOTIFICATION_CHANNEL_ID"
        const val NOTIFICATION_ID = 128042
        const val NOTIFICATION_TAG = "INCOMING_CALL_NOTIFICATION_TAG"
        const val NOTIFICATION_CHANNEL_NAME = "INCOMING_CALL_NOTIFICATION"

        const val INCOMING_ACTIVITY_ACTION_KEY = "INCOMING_ACTIVITY_ANSWER_ACTION"
        const val INCOMING_ACTIVITY_ANSWER_ACTION = "INCOMING_ACTIVITY_ANSWER_ACTION"
        const val INCOMING_ACTIVITY_REJECT_ACTION = "INCOMING_ACTIVITY_REJECT_ACTION"

        const val INCOMING_ACTIVITY_OPEN_REQUEST_KEY = 10
        const val INCOMING_ACTIVITY_ANSWER_REQUEST_KEY = 11
        const val INCOMING_ACTIVITY_REJECT_REQUEST_KEY = 12

        fun clearNotification(context: Context) {
            val notificationManager = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(NOTIFICATION_TAG, NOTIFICATION_ID)
        }
    }

    init {
        if (Build.VERSION.SDK_INT < 26) {
            //createNotification(context)
        } else {
            createNotificationChannel(context)
        }
    }

    fun postIncomingCallNotification(context: Context, callee: String) {
        Timber.tag("com.veglad.callapp").d("postIncomingCallNotification")
        // Create an intent which triggers your fullscreen incoming call user interface.
        val intent = IntentFactory.getIncomingCallActivityItent(context)
        val notification = initNotification(intent, context, callee)

        // Set notification as insistent to cause your ringtone to loop.
//        notification.flags = notification.flags or Notification.FLAG_INSISTENT

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager?.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        val mgr = context.getSystemService(NotificationManager::class.java)
        mgr!!.createNotificationChannel(channel)
    }

    private fun initNotification(intent: Intent, context: Context, callee: String): Notification {
        // Build the notification as an ongoing high priority item; this ensures it will show as
        // a heads up notification which slides down over top of the current content.
        val builder = if (Build.VERSION.SDK_INT < 26) {
            Notification.Builder(context).apply {
                setPriority(Notification.PRIORITY_HIGH)
            }
        } else {
            Notification.Builder(
                context,
                NOTIFICATION_CHANNEL_ID
            )
        }
        builder.setOngoing(true)
        val pendingIntent = PendingIntent.getActivity(context, INCOMING_ACTIVITY_OPEN_REQUEST_KEY, intent, 0)

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

        initActions(intent, context, builder)
        builder.setAutoCancel(true)

        return builder.build()
    }

    private fun initActions(intent: Intent, context: Context, builder: Notification.Builder) {
        intent.putExtra(INCOMING_ACTIVITY_ACTION_KEY, INCOMING_ACTIVITY_ANSWER_ACTION)
        val answerPendingIntent =
            PendingIntent.getActivity(context, INCOMING_ACTIVITY_ANSWER_REQUEST_KEY, intent, 0)
        val answerActionTitle =
            context.getString(R.string.incoming_call_notification_answer_action_title)
        val answerAction =
            Notification.Action.Builder(null, answerActionTitle, answerPendingIntent).build()
        builder.addAction(answerAction)

        intent.putExtra(INCOMING_ACTIVITY_ACTION_KEY, INCOMING_ACTIVITY_REJECT_ACTION)
        val rejectPendingIntent =
            PendingIntent.getActivity(context, INCOMING_ACTIVITY_REJECT_REQUEST_KEY, intent, 0)
        val rejectActionTitle =
            context.getString(R.string.incoming_call_notification_reject_action_title)
        val rejectAction =
            Notification.Action.Builder(null, rejectActionTitle, rejectPendingIntent).build()
        builder.addAction(rejectAction)
    }
}