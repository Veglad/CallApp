package com.veglad.callapp

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.telecom.Call
import android.telecom.InCallService
import android.util.Log
import com.veglad.callapp.connection.IncomingCallNotification
import timber.log.Timber

@TargetApi(Build.VERSION_CODES.M)
class CallService : InCallService() {

  companion object {
    private const val LOG_TAG = "CallService"
  }

  override fun onCallAdded(call: Call) {
    super.onCallAdded(call)
    Timber.tag("com.veglad.callapp").d( "onCallAdded: $call")
    call.registerCallback(callCallback)
    val notification = IncomingCallNotification(this)
    notification.postIncomingCallNotification(this, "Some Test Name")
    CallManager.updateCall(call)
  }

  override fun onCallRemoved(call: Call) {
    super.onCallRemoved(call)
    Timber.tag("com.veglad.callapp").d("onCallRemoved: $call")
    call.unregisterCallback(callCallback)
    CallManager.updateCall(null)
  }

  private val callCallback = object : Call.Callback() {
    override fun onStateChanged(call: Call, state: Int) {
      Timber.tag("com.veglad.callapp").d("Call.Callback onStateChanged: $call, state: $state")
      CallManager.updateCall(call)
    }
  }

}