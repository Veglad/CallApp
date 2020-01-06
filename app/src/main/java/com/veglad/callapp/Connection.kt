package com.veglad.callapp

import android.telecom.Connection
import android.telecom.DisconnectCause
import android.telecom.DisconnectCause.LOCAL

//https://developer.android.com/reference/android/telecom/Connection.html
class Connection : Connection() {
    override fun onShowIncomingCallUi() {
        //https://developer.android.com/reference/android/telecom/Connection.html#onShowIncomingCallUi()
    }

    override fun onAnswer() {
//        setActive()
    }

    override fun onReject() {
//        destroy()
    }

    override fun onDisconnect() {
//        setDisconnected(DisconnectCause(LOCAL))
//        destroy()
    }
}