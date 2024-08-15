package leoh.screenshot.protector

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
import leoh.screenshot.protector.listener.OnDecorViewDetachListener
import leoh.screenshot.protector.listener.OnDecorViewFocusChangeListener
import leoh.screenshot.protector.navigation.OSUtils
import leoh.screenshot.protector.navigation.isGesture
import leoh.screenshot.protector.strategy.BlurStrategy
import leoh.screenshot.protector.strategy.InsertDecorViewStrategy
import java.lang.ref.WeakReference

private const val TAG = "ScreenshotProtector"

@RequiresApi(Build.VERSION_CODES.Q)
internal class AdvanceScreenshotProtector(
    private val activity: ComponentActivity,
) : IScreenshotProtector,
    ViewTreeObserver.OnWindowFocusChangeListener,
    DefaultLifecycleObserver {
    private val activityDecorView: ViewGroup by lazy { activity.window.decorView as ViewGroup }
    private val decorViewInspector = DecorViewInspector.getInstance()
    private val decorViews = mutableListOf<WeakReference<View>>()
    private val blurStrategy: BlurStrategy = InsertDecorViewStrategy(activity)

    override fun protect() {
        Log.d(TAG, "protect: $activity")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.setRecentsScreenshotEnabled(false)
        } else if (OSUtils.isPixel() || activity.isGesture) {
            activity.window.addFlags(LayoutParams.FLAG_SECURE)
        }
        activityDecorView.viewTreeObserver.addOnWindowFocusChangeListener(this)
        activity.lifecycle.addObserver(this)
    }

    private fun release() {
        activityDecorView.viewTreeObserver.removeOnWindowFocusChangeListener(this)
        activity.lifecycle.removeObserver(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        Log.d(TAG, "onWindowFocusChanged: $hasFocus")
        if (hasFocus) {
            hideBlurView()
        } else {
            val topDecorView = decorViewInspector.getFocusedDecorViewInfo(activity) ?: return
            if (topDecorView.decorView == activityDecorView && !activity.isFinishing) {
                Log.d(TAG, "Activity is losing focus")
                showBlurView()
            } else {
                Log.d(TAG, "Activity show dialog")
                manageDialogEvents(topDecorView)
            }
        }
    }

    private fun manageDialogEvents(viewInfo: DecorViewInfo) {
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
                    } else if (!activity.isFinishing) {
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
            "Activity: activity=$activity, decor=$activityDecorView, focus=${activityDecorView.hasWindowFocus()}",
        )
        val decorViews = decorViewInspector.getDecorViewInfos(activity)
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
        val viewInfos = decorViewInspector.getDecorViewInfos(activity)
        for (info in viewInfos) {
            if (info.decorView != activityDecorView) {
                manageDialogEvents(info)
            }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.d(TAG, "onPause: $owner")
        if (!activity.isFinishing) {
            showBlurView()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.d(TAG, "onDestroy: $owner")
        release()
    }

    private fun showBlurView() {
        Log.d(TAG, "showBlurView $activity")
        logWindows()
        if (!blurStrategy.isShowing) {
            val viewInfo = decorViewInspector.getFocusedDecorViewInfo(activity) ?: return
            blurStrategy.showBlur(viewInfo)
        }
    }

    private fun hideBlurView() {
        Log.d(TAG, "hideBlurView $activity")
        logWindows()
        if (blurStrategy.isShowing) {
            blurStrategy.hideBlur()
        }
    }
}
