package com.veglad.callapp

import androidx.appcompat.app.AppCompatActivity
import com.veglad.callapp.data_driven.Configurator
import com.veglad.callapp.data_driven.DataDrivenApp
import com.veglad.callapp.telephony.CallManager
import com.veglad.callapp.view.IncomingCallActivity
import timber.log.Timber
import timber.log.Timber.DebugTree


class App : DataDrivenApp(), Configurator {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    override fun configure(activity: AppCompatActivity): Boolean {
        var isConfigured = true
        when (activity) {
            is IncomingCallActivity -> IncomingCallController(activity, CallManager)
            is MainActivity -> {}
            else -> isConfigured = false
        }

        return isConfigured
    }
}