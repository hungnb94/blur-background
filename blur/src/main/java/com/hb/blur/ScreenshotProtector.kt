package com.hb.blur

import androidx.core.app.ComponentActivity

object ScreenshotProtector {
    fun protect(activity: ComponentActivity) {
        ScreenshotActivityProtector(activity).protect()
    }
}
