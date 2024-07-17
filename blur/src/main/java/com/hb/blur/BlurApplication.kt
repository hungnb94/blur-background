package com.hb.blur

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

private const val TAG = "BlurApplication"

open class BlurApplication :
    Application(),
    Application.ActivityLifecycleCallbacks {
    override fun onCreate() {
        super.onCreate()
//        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?,
    ) {
        Log.d(TAG, "onActivityCreated: $activity")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(TAG, "onActivityStarted: $activity")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(TAG, "onActivityResumed: $activity")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d(TAG, "onActivityPaused: $activity")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d(TAG, "onActivityStopped: $activity")
    }

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle,
    ) {
        Log.d(TAG, "onActivitySaveInstanceState: $activity")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(TAG, "onActivityDestroyed: $activity")
    }
}
