package com.veglad.callapp.telephony

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.telecom.Call
import android.telecom.InCallService
import com.veglad.callapp.helpers.IntentFactory
import com.veglad.callapp.view.Call.Status
import com.veglad.callapp.view.mapCallFrom
import timber.log.Timber

@TargetApi(Build.VERSION_CODES.M)
class CallService : InCallService() {

  override fun onCallAdded(call: Call) {
    super.onCallAdded(call)
    Timber.tag("com.veglad.callapp").d( "onCallAdded: $call")
    call.registerCallback(callCallback)
    CallManager.updateCall(call)

    showUserInterface(call, this)
  }

  private fun showUserInterface(call: Call, context: Context) {
    if (isUserCalls(call)) {
      showDialingActivity(context)
    } else {
      showNotification(call, context)
    }
  }

    private fun isUserCalls(call: Call) = mapCallFrom(call).status == Status.CONNECTING

    private fun showDialingActivity(context: Context) {
    startActivity(IntentFactory.getIncomingCallActivityItent(context))
  }

  private fun showNotification(call: Call, context: Context) {
    val notification = IncomingCallNotification(context)
    notification.postIncomingCallNotification(context, call.details.handle.schemeSpecificPart)
  }

  override fun onCallRemoved(call: Call) {
    super.onCallRemoved(call)
    Timber.tag("com.veglad.callapp").d("onCallRemoved: $call")
    call.unregisterCallback(callCallback)
    CallManager.updateCall(null)
    IncomingCallNotification.clearNotification(this)
  }

  private val callCallback = object : Call.Callback() {
    override fun onStateChanged(call: Call, state: Int) {
      Timber.tag("com.veglad.callapp").d("Call.Callback onStateChanged: $call, state: $state")
      CallManager.updateCall(call)
    }
  }

}