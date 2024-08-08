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
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import leoh.screenshot.protector.listener.OnDecorViewDetachListener
import leoh.screenshot.protector.listener.OnDecorViewFocusChangeListener
import leoh.screenshot.protector.navigation.OSUtils
import leoh.screenshot.protector.navigation.isGesture
import java.lang.ref.WeakReference

private const val TAG = "ScreenshotProtector"

@RequiresApi(Build.VERSION_CODES.Q)
class ScreenshotProtector(
    private val activity: ComponentActivity,
) : ViewTreeObserver.OnWindowFocusChangeListener,
    DefaultLifecycleObserver {
    private val contentView: ViewGroup
        get() {
            return activity.window.decorView as ViewGroup
        }
    private val blurDialogView = View(activity)
    private val blurActivityView = View(activity)
    private val decorViewInspector = DecorViewInspector.getInstance()
    private val decorViews = mutableListOf<WeakReference<View>>()

    fun protect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.setRecentsScreenshotEnabled(false)
        } else if (OSUtils.isPixel() || activity.isGesture) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
        contentView.viewTreeObserver.addOnWindowFocusChangeListener(this)
        blurDialogView.viewTreeObserver.addOnWindowFocusChangeListener(this)
        activity.lifecycle.addObserver(this)
    }

    private fun release() {
        contentView.viewTreeObserver.removeOnWindowFocusChangeListener(this)
        blurDialogView.viewTreeObserver.removeOnWindowFocusChangeListener(this)
        activity.lifecycle.removeObserver(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        Log.d(TAG, "onWindowFocusChanged: $hasFocus")
        if (hasFocus) {
            hideBlurView()
        } else {
            val topDecorView = decorViewInspector.getFocusedDecorViewInfo()
            if (topDecorView != null) {
                if (topDecorView.activity == activity) {
                    if (topDecorView.decorView == activity.window.decorView) {
                        Log.d(TAG, "Activity is losing focus")
                        showBlurView()
                    } else {
                        Log.d(TAG, "Activity show dialog")
                        manageDialogListener(topDecorView)
                    }
                } else {
                    Log.d(TAG, "Opening another activity")
                }
            }
        }
        logWindows()
    }

    private fun manageDialogListener(viewInfo: DecorViewInfo) {
        val decorView = viewInfo.decorView
        if (decorViews.any { it.get() === decorView }) return
        decorViews.add(WeakReference(decorView))

        val focusChangeListener =
            object : OnDecorViewFocusChangeListener(decorView) {
                override fun onDecorViewFocusChanged(
                    view: View,
                    hasFocus: Boolean,
                ) {
                    Log.d(TAG, "onDecorViewFocusChanged: view=$decorView, focus=$hasFocus")
                    if (hasFocus) {
                        hideBlurView()
                    } else {
                        showBlurView()
                    }
                }
            }
        decorView.viewTreeObserver.addOnWindowFocusChangeListener(focusChangeListener)
        val detachListener =
            object : OnDecorViewDetachListener(decorView) {
                override fun onDecorViewDetached(view: View) {
                    Log.d(TAG, "onDecorViewDetached: $view")
                    view.viewTreeObserver.removeOnWindowFocusChangeListener(focusChangeListener)
                    view.viewTreeObserver.removeOnWindowAttachListener(this)
                    decorViews.removeAll { it.get() == null || it.get() == view }
                }
            }
        decorView.viewTreeObserver.addOnWindowAttachListener(detachListener)
    }

    fun onTopResumedActivityChanged(topResumedActivity: Boolean) {
        Log.d(TAG, "onTopResumedActivityChanged: $topResumedActivity")
        if (topResumedActivity) {
            hideBlurView()
        } else {
            showBlurView()
        }
    }

    private fun logWindows() {
        Log.d(
            TAG,
            "Activity: activity=$activity, decor=${activity.window.decorView}, focus=${activity.window.decorView.hasWindowFocus()}",
        )
        val decorViews = decorViewInspector.getDecorViewInfos()
        for (view in decorViews) {
            Log.d(
                TAG,
                "Window:   activity=${view.activity}, decor=${view.decorView}, focus=${view.decorView.hasWindowFocus()}",
            )
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.d(TAG, "onResume $owner")
        hideBlurView()
        val viewInfos = decorViewInspector.getDecorViewInfos()
        for (info in viewInfos) {
            if (!info.isActivityDecorView) {
                manageDialogListener(info)
            }
        }
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
        if (blurDialogView.parent == null && blurActivityView.parent == null) {
            val backgroundColor =
                if (activity.isNightMode) {
                    Color.BLACK
                } else {
                    Color.WHITE
                }
            val params =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
            val viewInfo = decorViewInspector.getFocusedDecorViewInfo()
            if (viewInfo?.isActivityDecorView == false) {
                Log.d(TAG, "showBlurView: on dialog")
                (viewInfo.decorView as ViewGroup).addView(
                    blurDialogView,
                    params,
                )
                blurDialogView.setBackgroundColor(backgroundColor)
            }
            (activity.window.decorView as ViewGroup).addView(
                blurActivityView,
                params,
            )
            blurActivityView.setBackgroundColor(backgroundColor)
        }
        logWindows()
    }

    private fun hideBlurView() {
        Log.d(TAG, "hideBlurView")
        (blurDialogView.parent as ViewGroup?)?.removeView(blurDialogView)
        (blurActivityView.parent as ViewGroup?)?.removeView(blurActivityView)
    }
}

val Context.isNightMode: Boolean
    get() {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
