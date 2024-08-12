package leoh.screenshot.protector

import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager.LayoutParams
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import leoh.screenshot.protector.extension.clone
import leoh.screenshot.protector.extension.isNightMode
import leoh.screenshot.protector.listener.OnDecorViewDetachListener
import leoh.screenshot.protector.listener.OnDecorViewFocusChangeListener
import leoh.screenshot.protector.navigation.OSUtils
import leoh.screenshot.protector.navigation.isGesture
import java.lang.ref.WeakReference

private const val TAG = "ScreenshotProtector"

@RequiresApi(Build.VERSION_CODES.Q)
internal class AdvanceScreenshotProtector(
    private val activity: ComponentActivity,
) : IScreenshotProtector,
    ViewTreeObserver.OnWindowFocusChangeListener,
    DefaultLifecycleObserver {
    private val contentView: ViewGroup
        get() {
            return activity.window.decorView as ViewGroup
        }
    private val blurView = View(activity)
    private val decorViewInspector = DecorViewInspector.getInstance()
    private val decorViews = mutableListOf<WeakReference<View>>()
    private var decorViewRestoreInfo: DecorViewRestoreInfo? = null

    override fun protect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.setRecentsScreenshotEnabled(false)
        } else if (OSUtils.isPixel() || activity.isGesture) {
            activity.window.addFlags(LayoutParams.FLAG_SECURE)
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
            val topDecorView = decorViewInspector.getFocusedDecorViewInfo(activity)
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
        Log.d(TAG, "showBlurView $activity")
        logWindows()
        if (blurView.parent == null) {
            addBlurView()
        }
    }

    private fun addBlurView() {
        val backgroundColor = getThemeBackgroundColor()
        val params =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        val viewInfo = decorViewInspector.getFocusedDecorViewInfo(activity)
        if (viewInfo?.activity == activity && !viewInfo.isActivityDecorView) {
            Log.d(TAG, "showBlurView: on dialog")
            val originLayoutParams = viewInfo.decorView.layoutParams as LayoutParams
            decorViewRestoreInfo =
                DecorViewRestoreInfo(
                    decorView = viewInfo.decorView,
                    layoutParams = originLayoutParams.clone(),
                    background = viewInfo.decorView.background,
                )
            makeDialogFullscreen(originLayoutParams.clone(), viewInfo, backgroundColor)
        } else {
            decorViewRestoreInfo = null
        }
        viewInfo?.decorView?.addView(blurView, params)
        blurView.setBackgroundColor(backgroundColor)
    }

    private fun makeDialogFullscreen(
        layoutParams: LayoutParams,
        viewInfo: DecorViewInfo,
        backgroundColor: Int,
    ) {
        val type = layoutParams.type
        layoutParams.copyFrom(activity.window.decorView.layoutParams as LayoutParams)
        layoutParams.type = type
        viewInfo.decorView.setBackgroundColor(backgroundColor)
        activity.windowManager.updateViewLayout(viewInfo.decorView, layoutParams)
    }

    private fun getThemeBackgroundColor() =
        if (activity.isNightMode) {
            Color.BLACK
        } else {
            Color.WHITE
        }

    private fun hideBlurView() {
        Log.d(TAG, "hideBlurView $activity")
        logWindows()
        val parentBlurView = blurView.parent
        if (parentBlurView != null) {
            (parentBlurView as ViewGroup).removeView(blurView)

            decorViewRestoreInfo?.let {
                it.decorView.background = it.background
                activity.windowManager.updateViewLayout(it.decorView, it.layoutParams)
            }
        }
    }
}
