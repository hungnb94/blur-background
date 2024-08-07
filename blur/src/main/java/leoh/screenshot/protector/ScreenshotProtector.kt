package leoh.screenshot.protector

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inspector.WindowInspector
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import leoh.screenshot.protector.navigation.OSUtils
import leoh.screenshot.protector.navigation.isGesture

private const val TAG = "ScreenshotProtector"

class ScreenshotProtector(
    private val activity: ComponentActivity,
) : ViewTreeObserver.OnWindowFocusChangeListener,
    DefaultLifecycleObserver {
    private val contentView: ViewGroup
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val decorViews = WindowInspector.getGlobalWindowViews()
                if (decorViews.isNotEmpty()) {
                    return decorViews.first().rootView as ViewGroup
                }
            }
            return activity.window.decorView as ViewGroup
        }
    private val blurView = View(activity)

    fun protect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.setRecentsScreenshotEnabled(false)
        } else if (OSUtils.isPixel() || activity.isGesture) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
        contentView.viewTreeObserver.addOnWindowFocusChangeListener(this)
        activity.lifecycle.addObserver(this)
    }

    private fun release() {
        contentView.viewTreeObserver.removeOnWindowFocusChangeListener(this)
        activity.lifecycle.removeObserver(this)
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

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.d(TAG, "onResume $owner")
        hideBlurView()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.d(TAG, "onPause: $owner")
        showBlurView()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.d(TAG, "onDestroy: $owner")
        release()
    }

    private fun showBlurView() {
        Log.d(TAG, "showBlurView")
        if (blurView.parent == null) {
            contentView.addView(
                blurView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            if (activity.isNightMode) {
                blurView.setBackgroundColor(Color.BLACK)
            } else {
                blurView.setBackgroundColor(Color.WHITE)
            }
        }
    }

    private fun hideBlurView() {
        Log.d(TAG, "hideBlurView")
        if (blurView.parent != null) {
            contentView.removeView(blurView)
        }
    }
}

val Context.isNightMode: Boolean
    get() {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
