package leoh.screenshot.protector.strategy

import android.app.Activity
import android.graphics.Color
import android.view.View
import leoh.screenshot.protector.DecorViewInfo
import leoh.screenshot.protector.extension.isNightMode

abstract class BlurStrategy(
    val activity: Activity,
) {
    abstract val blurView: View

    abstract fun showBlur(viewInfo: DecorViewInfo)

    abstract fun hideBlur()

    val isShowing: Boolean
        get() = blurView.parent != null

    fun getThemeBackgroundColor() =
        if (activity.isNightMode) {
            Color.BLACK
        } else {
            Color.WHITE
        }
}
