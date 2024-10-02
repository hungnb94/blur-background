package leoh.screenshot.protector

import android.os.Build
import androidx.activity.ComponentActivity
import leoh.screenshot.protector.log.DefaultLogger
import leoh.screenshot.protector.log.Logger

object ScreenshotProtector {
    @JvmStatic
    fun protect(activity: ComponentActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AdvanceScreenshotProtector(activity).protect()
        } else {
            SimpleScreenshotProtector(activity).protect()
        }
    }

    @JvmStatic
    var logger: Logger = DefaultLogger()
}
