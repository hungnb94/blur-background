package leoh.screenshot.protector

import androidx.activity.ComponentActivity

object ScreenshotProtector {
    fun protect(activity: ComponentActivity) {
        ScreenshotActivityProtector(activity).protect()
    }
}
