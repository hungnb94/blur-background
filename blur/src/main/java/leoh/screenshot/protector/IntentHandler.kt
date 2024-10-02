package leoh.screenshot.protector

import android.content.Intent

class IntentHandler(
    private val packageName: String,
) {
    fun checkWhiteList(intent: Intent): Boolean = isSamePackageName(intent)

    private fun isSamePackageName(intent: Intent) = intent.component?.packageName == packageName
}
