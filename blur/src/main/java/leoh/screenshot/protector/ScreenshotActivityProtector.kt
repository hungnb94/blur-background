package leoh.screenshot.protector

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import leoh.screenshot.protector.navigation.OSUtils
import leoh.screenshot.protector.navigation.isGesture

private const val TAG = "ScreenshotActivityProtector"

class ScreenshotActivityProtector(
    private val activity: ComponentActivity,
) : ViewTreeObserver.OnWindowFocusChangeListener,
    DefaultLifecycleObserver {
    private var blurDialog: Dialog? = null
    private val blurView = View(activity)

    fun protect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.setRecentsScreenshotEnabled(false)
        } else if (OSUtils.isPixel() || activity.isGesture) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
        activity.window.decorView.viewTreeObserver
            .addOnWindowFocusChangeListener(this)
        activity.lifecycle.addObserver(this)
        blurDialog =
            AlertDialog
                .Builder(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
//                .Builder(activity, R.style.FullScreenDialog)
                .setView(blurView)
                .create()
    }

    private fun release() {
        activity.window.decorView.viewTreeObserver
            .removeOnWindowFocusChangeListener(this)
        activity.lifecycle.removeObserver(this)
        blurDialog?.dismiss()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        Log.d(TAG, "onWindowFocusChanged: $hasFocus")
        if (hasFocus) {
            hideBlurView()
        } else {
            showBlurView()
        }
    }

    fun onTopResumedActivityChanged(topResumedActivity: Boolean) {
        Log.d(TAG, "onTopResumedActivityChanged: $topResumedActivity")
        if (topResumedActivity) {
            hideBlurView()
        } else {
            showBlurView()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.d(TAG, "onDestroy: $owner")
        release()
    }

    private fun showBlurView() {
        Log.d(TAG, "showBlurView")
        if (blurDialog?.isShowing != true) {
            if (activity.isNightMode) {
                blurView.setBackgroundColor(Color.BLACK)
            } else {
                blurView.setBackgroundColor(Color.WHITE)
            }
            blurDialog?.show()
        }
    }

    private fun hideBlurView() {
        Log.d(TAG, "hideBlurView")
        if (blurDialog?.isShowing == true) {
            blurDialog?.dismiss()
        }
    }
}

val Context.isNightMode: Boolean
    get() {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
