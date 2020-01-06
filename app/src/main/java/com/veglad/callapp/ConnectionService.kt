package com.veglad.callapp

import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager.PRESENTATION_ALLOWED

class ConnectionService : ConnectionService() {

    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        return Connection().apply {
//            connectionProperties = PROPERTY_SELF_MANAGED | API 25
//            setConnectionCapabilities(int) if app supports onHold
            setCallerDisplayName("TEST NAME", PRESENTATION_ALLOWED)
        }
    }

    override fun onCreateOutgoingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request)
    }

    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        return Connection().apply {
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
        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request)
    }
}
