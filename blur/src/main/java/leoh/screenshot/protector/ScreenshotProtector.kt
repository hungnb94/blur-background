package leoh.screenshot.protector

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
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
    DefaultLifecycleObserver,
    ViewTreeObserver.OnGlobalLayoutListener {
    private val contentView: ViewGroup
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val decorViews = WindowInspector.getGlobalWindowViews()
                if (decorViews.isNotEmpty()) {
                    return decorViews.first() as ViewGroup
                }
            }
            return activity.window.decorView as ViewGroup
        }
    private val blurView = View(activity)
    private val decorViewInspector = DecorViewInspector.getInstance()

    fun protect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.setRecentsScreenshotEnabled(false)
        } else if (OSUtils.isPixel() || activity.isGesture) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
        contentView.viewTreeObserver.addOnWindowFocusChangeListener(this)
        contentView.viewTreeObserver.addOnGlobalLayoutListener(this)
        activity.lifecycle.addObserver(this)
    }

    private fun release() {
        contentView.viewTreeObserver.removeOnWindowFocusChangeListener(this)
        contentView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        activity.lifecycle.removeObserver(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        Log.d(TAG, "onWindowFocusChanged: $hasFocus")
        if (hasFocus) {
            hideBlurView()
        } else {
//            showBlurView()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val topDecorView = decorViewInspector.getTopDecorViewInfo()
                if (topDecorView != null) {
                    if (topDecorView.activity == activity) {
                        if (topDecorView.decorView == activity.window.decorView) {
                            Log.d(TAG, "Current activity lost focus")
                            showBlurView()
                        } else {
                            Log.d(TAG, "Current activity show dialog")
                            topDecorView.decorView.viewTreeObserver.addOnWindowFocusChangeListener {
                                Log.d(TAG, "onWindowFocusChanged: dialog has window focus=$it")
                                if (!it) {
                                    showBlurView()
                                }
                            }
                        }
                    } else {
                        Log.d(TAG, "Opening another activity")
                    }
                }
            }
        }
        logWindows()
    }

    fun onTopResumedActivityChanged(topResumedActivity: Boolean) {
        Log.d(TAG, "onTopResumedActivityChanged: $topResumedActivity")
        if (topResumedActivity) {
            hideBlurView()
        } else {
            showBlurView()
        }
//        logWindows()
    }

    private fun logWindows() {
        Log.d(
            TAG,
            "Activity:  activity=$activity, decorView=${activity.window.decorView}, hasWindowFocus=${activity.window.decorView.hasWindowFocus()}",
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val decorViews = decorViewInspector.getDecorViewInfos()
            for (view in decorViews) {
                Log.d(
                    TAG,
                    "Window:    activity=${view.activity}, decorView=${view.decorView}, hasWindowFocus=${view.decorView.hasWindowFocus()}",
                )
            }
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.d(TAG, "onResume $owner")
        hideBlurView()
        logWindows()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.d(TAG, "onPause: $owner")
        showBlurView()
        logWindows()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.d(TAG, "onDestroy: $owner")
        release()
    }

    private fun showBlurView() {
        Log.d(TAG, "showBlurView")
        if (blurView.parent == null) {
//            val contentView =
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    val decorView = decorViewInspector.getTopDecorView()
//                    if (decorView?.isDialog == true) {
//                        val params = WindowManager.LayoutParams(0, 0)
//                        params.copyFrom(decorView.decorView.layoutParams as WindowManager.LayoutParams?)
//                        params.width = WindowManager.LayoutParams.MATCH_PARENT
//                        params.height = WindowManager.LayoutParams.MATCH_PARENT
//                        activity.windowManager.updateViewLayout(decorView.decorView, params)
//                    }
//                    decorView?.contentView as ViewGroup
//                } else {
//                    contentView
//                }
//            contentView.addView(
//                blurView,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//            )
            val params = WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_WALLPAPER)
            activity.windowManager.addView(blurView, params)
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
            val contentView = blurView.parent as ViewParent
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    decorViewInspector.getTopDecorView()?.contentView as ViewGroup
//                } else {
//                    contentView
//                }
//            contentView.removeView(blurView)
            activity.windowManager.removeView(blurView)
        }
    }

    override fun onGlobalLayout() {
        Log.d(TAG, "onGlobalLayout")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val viewInfo = decorViewInspector.getTopDecorViewInfo()
            Log.d(TAG, "Show dialog: ${viewInfo?.isActivityDecorView}")
        }
    }
}

val Context.isNightMode: Boolean
    get() {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
