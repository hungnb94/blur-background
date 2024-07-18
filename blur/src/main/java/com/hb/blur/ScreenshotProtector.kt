package com.hb.blur

import androidx.activity.ComponentActivity

object ScreenshotProtector {
    fun protect(activity: ComponentActivity) {
        ScreenshotActivityProtector(activity).protect()
    }
}
