package leoh.screenshot.protector

import android.app.Activity
import android.view.ViewGroup

data class DecorViewInfo(
    val decorView: ViewGroup,
) {
    val contentView: ViewGroup? by lazy { decorView.findViewById(android.R.id.content) }
    val activity: Activity? by lazy { contentView?.activity }
    val isActivityDecorView: Boolean = decorView == activity?.window?.decorView
}
