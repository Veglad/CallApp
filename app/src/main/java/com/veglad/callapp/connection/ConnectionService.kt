package com.veglad.callapp.connection

import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager.PRESENTATION_ALLOWED
import timber.log.Timber

class ConnectionService : ConnectionService() {

    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Timber.tag("com.veglad.callapp").d("onCreateOutgoingConnection")
        return Connection(applicationContext).apply {
//            connectionProperties = PROPERTY_SELF_MANAGED | API 25
//            setConnectionCapabilities(int) if app supports onHold
            setCallerDisplayName("TEST NAME", PRESENTATION_ALLOWED)
        }
    }

    override fun onCreateOutgoingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        Timber.tag("com.veglad.callapp").e("onCreateOutgoingConnectionFailed")
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request)
    }

    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Timber.tag("com.veglad.callapp").d("onCreateIncomingConnection")
        return Connection(applicationContext).apply {
//            connectionProperties = PROPERTY_SELF_MANAGED | API 25
//            setConnectionCapabilities(int) if app supports onHold
            setCallerDisplayName("TEST NAME", PRESENTATION_ALLOWED)
            // To specify the phone number or address of the incoming call
            // setAddress()
        }
    }

    override fun onCreateIncomingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        Timber.tag("com.veglad.callapp").e("onCreateIncomingConnectionFailed")
        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request)
    }
}
