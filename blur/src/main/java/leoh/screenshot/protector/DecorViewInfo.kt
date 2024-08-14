package leoh.screenshot.protector

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import leoh.screenshot.protector.extension.activity

data class DecorViewInfo(
    val decorView: View,
) {
    private val contentView: ViewGroup? by lazy { decorView.findViewById(android.R.id.content) }
    val activity: Activity? by lazy { decorView.activity ?: contentView?.activity }
}
