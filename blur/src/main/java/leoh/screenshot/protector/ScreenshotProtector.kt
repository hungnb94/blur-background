package leoh.screenshot.protector

import androidx.activity.ComponentActivity

object ScreenshotProtector {
    fun protect(activity: ComponentActivity): ScreenshotActivityProtector {
        val protector = ScreenshotActivityProtector(activity)
        protector.protect()
        return protector
    }
}
