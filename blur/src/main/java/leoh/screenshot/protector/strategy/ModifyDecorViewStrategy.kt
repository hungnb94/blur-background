package leoh.screenshot.protector.strategy

import android.app.Activity
import android.view.ViewGroup
import android.view.ViewManager
import android.view.WindowManager.LayoutParams
import leoh.screenshot.protector.DecorViewInfo
import leoh.screenshot.protector.DecorViewRestoreInfo
import leoh.screenshot.protector.extension.clone

class ModifyDecorViewStrategy(
    activity: Activity,
) : BlurStrategy(activity) {
    private var decorViewRestoreInfo: DecorViewRestoreInfo? = null

    override fun showBlur(viewInfo: DecorViewInfo) {
        if (blurView.parent == null) {
            addBlurView(viewInfo)
        }
    }

    private fun addBlurView(viewInfo: DecorViewInfo) {
        val backgroundColor = getThemeBackgroundColor()
        val params =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
//        makeDialogFullscreenIfNeeded(viewInfo)
        if (viewInfo.decorView is ViewManager) {
            viewInfo.decorView.addView(blurView, params)
            blurView.setBackgroundColor(backgroundColor)
        }
    }

    private fun setDialogFullscreen(
        layoutParams: LayoutParams,
        viewInfo: DecorViewInfo,
        backgroundColor: Int,
    ) {
        val type = layoutParams.type
        layoutParams.copyFrom(activity.window.attributes)
        layoutParams.type = type
        viewInfo.decorView.setBackgroundColor(backgroundColor)
        activity.windowManager.updateViewLayout(viewInfo.decorView, layoutParams)
    }

    private fun saveDialogInfo(viewInfo: DecorViewInfo) {
        decorViewRestoreInfo =
            DecorViewRestoreInfo(
                decorView = viewInfo.decorView,
                layoutParams = (viewInfo.decorView.layoutParams as LayoutParams).clone(),
                background = viewInfo.decorView.background,
            )
    }

    override fun hideBlur(viewInfo: DecorViewInfo) {
        val parentBlurView = blurView.parent
        if (parentBlurView != null && parentBlurView is ViewManager) {
            restoreDialogDecorView()
            parentBlurView.removeView(blurView)
        }
    }

    private fun restoreDialogDecorView() {
        decorViewRestoreInfo?.let {
            it.decorView.background = it.background
            activity.windowManager.updateViewLayout(it.decorView, it.layoutParams)
        }
        decorViewRestoreInfo = null
    }
}
