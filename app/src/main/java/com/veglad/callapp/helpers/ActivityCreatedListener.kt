package com.veglad.callapp.helpers

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ActivityCreatedListener(private val listener: AppCompatActivity.() -> Unit) :
    Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity?) = Unit

    override fun onActivityResumed(activity: Activity?) = Unit

    override fun onActivityStarted(activity: Activity?) {
        require(activity is AppCompatActivity)
        listener(activity)
    }

    override fun onActivityDestroyed(activity: Activity?) = Unit

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) = Unit

    override fun onActivityStopped(activity: Activity?) = Unit

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }
}
