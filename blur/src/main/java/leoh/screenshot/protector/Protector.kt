package leoh.screenshot.protector

import android.os.Build
import androidx.activity.ComponentActivity

object Protector {
    fun protect(activity: ComponentActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ScreenshotProtector(activity).protect()
        } else {
            SimpleScreenshotProtector(activity).protect()
        }
    }
}
