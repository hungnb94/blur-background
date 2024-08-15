package leoh.screenshot.protector.strategy

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.view.WindowManager.LayoutParams
import leoh.screenshot.protector.DecorViewInfo
import leoh.screenshot.protector.DecorViewRestoreInfo
import leoh.screenshot.protector.extension.clone

class ModifyDecorViewStrategy(
    activity: Activity,
) : BlurStrategy(activity) {
    override val blurView: View = View(activity)
    private var dialogRestoreInfo: DecorViewRestoreInfo? = null

    override fun showBlur(viewInfo: DecorViewInfo) {
        if (blurView.parent == null) {
            addBlurView(viewInfo)
        }
    }

    private fun addBlurView(viewInfo: DecorViewInfo) {
        val backgroundColor = getThemeBackgroundColor()
        val params = matchParentLayoutParams()
        makeDialogFullscreenIfNeeded(viewInfo, backgroundColor)
        if (viewInfo.decorView is ViewManager) {
            viewInfo.decorView.addView(blurView, params)
            blurView.setBackgroundColor(backgroundColor)
        }
    }

    private fun makeDialogFullscreenIfNeeded(
        viewInfo: DecorViewInfo,
        backgroundColor: Int,
    ) {
        if (viewInfo.isDialog) {
            dialogRestoreInfo = getDialogInfo(viewInfo)
            setDialogFullscreen(
                viewInfo = viewInfo,
                backgroundColor = backgroundColor,
            )
        } else {
            dialogRestoreInfo = null
        }
    }

    private fun matchParentLayoutParams() =
        ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )

    private fun setDialogFullscreen(
        viewInfo: DecorViewInfo,
        backgroundColor: Int,
    ) {
        val layoutParams = viewInfo.decorView.layoutParams as LayoutParams
        val type = layoutParams.type
        layoutParams.copyFrom(activity.window.attributes)
        layoutParams.type = type
        viewInfo.decorView.setBackgroundColor(backgroundColor)
        activity.windowManager.updateViewLayout(viewInfo.decorView, layoutParams)
    }

    private fun getDialogInfo(viewInfo: DecorViewInfo): DecorViewRestoreInfo =
        DecorViewRestoreInfo(
            decorView = viewInfo.decorView,
            layoutParams = (viewInfo.decorView.layoutParams as LayoutParams).clone(),
            background = viewInfo.decorView.background,
        )

    override fun hideBlur() {
        val parentBlurView = blurView.parent
        if (parentBlurView != null && parentBlurView is ViewManager) {
            restoreDialogDecorView()
            parentBlurView.removeView(blurView)
        }
    }

    private fun restoreDialogDecorView() {
        dialogRestoreInfo?.let {
            it.decorView.background = it.background
            activity.windowManager.updateViewLayout(it.decorView, it.layoutParams)
        }
        dialogRestoreInfo = null
    }
}
