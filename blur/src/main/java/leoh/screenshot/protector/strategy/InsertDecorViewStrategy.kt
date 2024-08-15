package leoh.screenshot.protector.strategy

import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager.LayoutParams
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import leoh.screenshot.protector.DecorViewInfo

@RequiresApi(Build.VERSION_CODES.P)
class InsertDecorViewStrategy(
    activity: Activity,
) : BlurStrategy(activity),
    ViewTreeObserver.OnWindowFocusChangeListener,
    ViewTreeObserver.OnWindowAttachListener {
    override val blurView: View = FrameLayout(activity)

    override fun showBlur(viewInfo: DecorViewInfo) {
        if (!isShowing) {
            addBlurView()
        }
    }

    private fun addBlurView() {
        val backgroundColor = getThemeBackgroundColor()
        val params = matchParentLayoutParams()
        activity.windowManager.addView(blurView, params)
        blurView.setBackgroundColor(backgroundColor)
        blurView.viewTreeObserver.addOnWindowFocusChangeListener(this)
        blurView.viewTreeObserver.addOnWindowAttachListener(this)
    }

    private fun matchParentLayoutParams(): LayoutParams {
        val params = LayoutParams(LayoutParams.TYPE_APPLICATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            params.fitInsetsTypes = 0
        }
        params.layoutInDisplayCutoutMode = LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        return params
    }

    override fun hideBlur() {
        if (isShowing) {
            removeViewListeners()
            activity.windowManager.removeView(blurView)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        Log.d(TAG, "onWindowFocusChanged: $hasFocus")
        if (hasFocus) {
            hideBlur()
        }
    }

    override fun onWindowAttached() {
        // do nothing
    }

    override fun onWindowDetached() {
        Log.d(TAG, "onWindowDetached")
        removeViewListeners()
    }

    private fun removeViewListeners() {
        blurView.viewTreeObserver.removeOnWindowFocusChangeListener(this)
        blurView.viewTreeObserver.removeOnWindowAttachListener(this)
    }

    companion object {
        private const val TAG = "InsertDecorViewStrategy"
    }
}
