package com.veglad.callapp.helpers

import android.content.Context
import android.content.Intent
import com.veglad.callapp.view.IncomingCallActivity

object IntentFactory {
    fun getIncomingCallActivityItent(context: Context): Intent {
        return Intent(Intent.ACTION_MAIN, null).apply {
            flags = Intent.FLAG_ACTIVITY_NO_USER_ACTION or Intent.FLAG_ACTIVITY_NEW_TASK
            setClass(context, IncomingCallActivity::class.java)
        }
    }
}