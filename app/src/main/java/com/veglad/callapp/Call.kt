package com.veglad.callapp

import android.telecom.Call as TelecomCall

data class Call(val status: Status, val callee: String?) {

    enum class Status {
        CONNECTING,
        DIALING,
        RINGING,
        ACTIVE,
        DISCONNECTED,
        UNKNOWN
    }
}

fun mapCallFrom(telecomCall: TelecomCall) = Call(
    status = mapCallStatusFromTelecomCall(telecomCall),
    callee = telecomCall.details.handle.schemeSpecificPart
)

private fun mapCallStatusFromTelecomCall(telecomCall: TelecomCall) = when(telecomCall.state) {
    TelecomCall.STATE_CONNECTING -> Call.Status.CONNECTING
    TelecomCall.STATE_DIALING -> Call.Status.DIALING
    TelecomCall.STATE_RINGING -> Call.Status.RINGING
    TelecomCall.STATE_ACTIVE -> Call.Status.ACTIVE
    TelecomCall.STATE_DISCONNECTED -> Call.Status.DISCONNECTED
    else -> Call.Status.UNKNOWN
}