package com.veglad.callapp.data_driven

import android.app.Application
import com.veglad.callapp.helpers.ActivityCreatedListener

abstract class DataDrivenApp : Application() {
    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(ActivityCreatedListener {
                val configurators = listOfNotNull(
                    this as? Configurator,
                    application as? Configurator
                )
                val isConfigured = configurators.any {
                    it.configure(this)
                }

                require(isConfigured) {
                    "Activity ${javaClass.name} is not configured"
                }
        })
    }
}
