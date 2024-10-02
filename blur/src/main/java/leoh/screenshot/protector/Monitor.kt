package leoh.screenshot.protector

import android.app.Instrumentation
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import leoh.screenshot.protector.listener.MonitorListener

@RequiresApi(Build.VERSION_CODES.O)
class Monitor(
    private val listener: MonitorListener,
) : Instrumentation.ActivityMonitor() {
    override fun onStartActivity(intent: Intent?): Instrumentation.ActivityResult? {
        listener.onStartActivity(intent)
        return null
    }
}
