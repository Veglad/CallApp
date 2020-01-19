package com.veglad.callapp.telephony

import android.annotation.TargetApi
import android.os.Build
import com.veglad.callapp.view.Call
import com.veglad.callapp.view.mapCallFrom
import android.telecom.Call as TelecomCall
import timber.log.Timber

@TargetApi(Build.VERSION_CODES.M)
object CallManager {

  private var currentCall: TelecomCall? = null
  private var onUpdateCall: ((call: Call) -> Unit)? = null

  fun setOnUpdateCall(onUpdateCall: ((call: Call) -> Unit)) {
    this.onUpdateCall = onUpdateCall
  }

  fun updateCall(telecomCall: TelecomCall?) {
    currentCall = telecomCall
    telecomCall?.let {
      val call = mapCallFrom(it)
      onUpdateCall?.invoke(call)
    }
  }

  fun cancelCall() {
    currentCall?.let {
      when (it.state) {
        TelecomCall.STATE_RINGING -> rejectCall()
        else               -> disconnectCall()
      }
    }
  }

  fun acceptCall() {
    Timber.tag("com.veglad.callapp").d("acceptCall")
    currentCall?.let {
      it.answer(it.details.videoState)
    }
  }

  private fun rejectCall() {
    Timber.tag("com.veglad.callapp").d("rejectCall")
    currentCall?.reject(false, "")
  }

  private fun disconnectCall() {
    Timber.tag("com.veglad.callapp").d("disconnectCall")
    currentCall?.disconnect()
  }
}
