package com.veglad.callapp.connection

import android.content.Context
import android.telecom.Connection
import android.telecom.DisconnectCause
import android.telecom.DisconnectCause.LOCAL
import android.telecom.DisconnectCause.REJECTED
import timber.log.Timber

//https://developer.android.com/reference/android/telecom/Connection.html
class Connection(private val context: Context) : Connection() {
    // https://developer.android.com/reference/android/telecom/PhoneAccount.html#CAPABILITY_SELF_MANAGED
    //https://developer.android.com/reference/android/telecom/Connection.html#onShowIncomingCallUi()
    override fun onShowIncomingCallUi() {
        Timber.tag("com.veglad.callapp").d( "onShowIncomingCallUi")
        val notification = IncomingCallNotification(context)
        notification.postIncomingCallNotification(context, "Some Test Name")
    }

    override fun onAnswer() {
        Timber.tag("com.veglad.callapp").d("onAnswer")
        setActive()
    }

    override fun onReject() {
        Timber.tag("com.veglad.callapp").d("onReject")
        setDisconnected(DisconnectCause(REJECTED))
        destroy()
    }

    override fun onDisconnect() {
        Timber.tag("com.veglad.callapp").d("onDisconnect")
        setDisconnected(DisconnectCause(LOCAL))
        destroy()
    }

    override fun onSilence() {
        super.onSilence()
    }
}