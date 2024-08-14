package leoh.screenshot.protector.strategy

import android.app.Activity
import android.graphics.Color
import android.view.View
import leoh.screenshot.protector.DecorViewInfo
import leoh.screenshot.protector.extension.isNightMode

abstract class BlurStrategy(
    val activity: Activity,
) {
    val blurView = View(activity)

    abstract fun showBlur(viewInfo: DecorViewInfo)

    abstract fun hideBlur(viewInfo: DecorViewInfo)

    fun getThemeBackgroundColor() =
        if (activity.isNightMode) {
            Color.BLACK
        } else {
            Color.WHITE
        }

    fun isDialog(viewInfo: DecorViewInfo): Boolean = viewInfo.decorView != activity.window.decorView
}
