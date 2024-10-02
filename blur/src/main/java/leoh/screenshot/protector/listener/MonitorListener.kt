package leoh.screenshot.protector.listener

import android.content.Intent

interface MonitorListener {
    fun onStartActivity(intent: Intent?)
}
