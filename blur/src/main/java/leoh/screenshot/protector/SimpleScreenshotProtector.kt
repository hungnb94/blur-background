package leoh.screenshot.protector

import android.os.Build
import android.view.WindowManager.LayoutParams
import androidx.activity.ComponentActivity

internal class SimpleScreenshotProtector(
    private val activity: ComponentActivity,
) : IScreenshotProtector {
    override fun protect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.setRecentsScreenshotEnabled(false)
        } else {
            activity.window.addFlags(LayoutParams.FLAG_SECURE)
        }
    }
}
