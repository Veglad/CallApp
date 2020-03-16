package com.veglad.callapp

import androidx.appcompat.app.AppCompatActivity
import com.veglad.callapp.data_driven.Configurator
import com.veglad.callapp.data_driven.DataDrivenApp
import com.veglad.callapp.db.Repository
import com.veglad.callapp.telephony.CallManager
import com.veglad.callapp.telephony.IncomingCallController
import com.veglad.callapp.view.IncomingCallActivity
import io.realm.Realm
import timber.log.Timber
import timber.log.Timber.DebugTree
import kotlin.coroutines.EmptyCoroutineContext


class App : DataDrivenApp(), Configurator {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        Realm.init(this)
        Repository.initDb()
    }

    override fun configure(activity: AppCompatActivity): Boolean {
        var isConfigured = true
        when (activity) {
            is IncomingCallActivity -> IncomingCallController(
                activity,
                CallManager,
                EmptyCoroutineContext
            )
            is MainActivity -> {}
            else -> isConfigured = false
        }

        return isConfigured
    }
}