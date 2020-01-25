package com.veglad.callapp.telephony

import com.veglad.callapp.data_driven.Command
import com.veglad.callapp.data_driven.DataDrivenActivity
import com.veglad.callapp.telephony.CallManager
import com.veglad.callapp.view.Call
import com.veglad.callapp.view.IncomingCallActivity.Props
import timber.log.Timber

class IncomingCallController(
    private var propsConsumer: DataDrivenActivity<Props>?,
    private val callManager: CallManager
) {

    private var callTime: Long = 0

    init {
        callManager.setOnUpdateCall(this::handleCall)
        callManager.call?.let { handleCall(it) }
    }

    private fun handleCall(call: Call) {
        Timber.tag("IncomingCallController").d("Updated")
        reduceState(call)
        propsConsumer?.renderProps(mapCallToProps(call))
    }

    private fun mapCallToProps(call: Call) : Props {
        return when(call.status) {
            Call.Status.CONNECTING -> Props.Connecting
            Call.Status.DIALING -> Props.Dialing(call.callee ?: "Unknown", Command { callManager.cancelCall() })
            Call.Status.RINGING -> Props.Ringing(call.callee ?: "Unknown", Command { callManager.acceptCall() }, Command { callManager.cancelCall() })
            Call.Status.ACTIVE-> Props.Active(call.callee ?: "Unknown", callTime, Command { callManager.cancelCall() })
            Call.Status.DISCONNECTED-> Props.Disconnected
            Call.Status.UNKNOWN-> Props.Unknown
        }
    }

    private fun reduceState(call: Call) {
        when(call.status) {
            Call.Status.DISCONNECTED-> {
                callTime = 0
            }
        }
    }
}