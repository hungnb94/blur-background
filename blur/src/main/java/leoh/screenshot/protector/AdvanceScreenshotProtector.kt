package leoh.screenshot.protector

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager.LayoutParams
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import leoh.screenshot.protector.ScreenshotProtector.logger
import leoh.screenshot.protector.listener.MonitorListener
import leoh.screenshot.protector.listener.OnDecorViewDetachListener
import leoh.screenshot.protector.listener.OnDecorViewFocusChangeListener
import leoh.screenshot.protector.navigation.OSUtils
import leoh.screenshot.protector.navigation.isGesture
import leoh.screenshot.protector.strategy.BlurStrategy
import leoh.screenshot.protector.strategy.ModifyDecorViewStrategy
import java.lang.ref.WeakReference
import java.lang.reflect.Field

private const val TAG = "ScreenshotProtector"

@RequiresApi(Build.VERSION_CODES.Q)
internal class AdvanceScreenshotProtector(
    private val activity: ComponentActivity,
) : IScreenshotProtector,
    ViewTreeObserver.OnWindowFocusChangeListener,
    DefaultLifecycleObserver,
    MonitorListener {
    private val activityDecorView: ViewGroup by lazy { activity.window.decorView as ViewGroup }
    private val decorViewInspector = DecorViewInspector.getInstance()
    private val decorViews = mutableListOf<WeakReference<View>>()
    private val blurStrategy: BlurStrategy = ModifyDecorViewStrategy(activity)
    private var instrumentation: Instrumentation? = null
    private val activityMonitor = Monitor(this)
    private val intentHandler = IntentHandler(activity.packageName)
    private var shouldIgnore = false

    override fun protect() {
        logger.d(TAG, "protect: $activity")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.setRecentsScreenshotEnabled(false)
        } else if (OSUtils.isPixel() || activity.isGesture) {
            activity.window.addFlags(LayoutParams.FLAG_SECURE)
        }
        activityDecorView.viewTreeObserver.addOnWindowFocusChangeListener(this)
        activity.lifecycle.addObserver(this)
        instrumentation = getInstrumentation(activity)
    }

    private fun getInstrumentation(activity: ComponentActivity): Instrumentation? {
        try {
            val field: Field = Activity::class.java.getDeclaredField("mInstrumentation")
            field.isAccessible = true
            return field.get(activity) as Instrumentation?
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
    }

    private fun release() {
        activityDecorView.viewTreeObserver.removeOnWindowFocusChangeListener(this)
        activity.lifecycle.removeObserver(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        logger.d(TAG, "onWindowFocusChanged: $hasFocus")
        if (hasFocus) {
            hideBlurView()
        } else {
            val topDecorView = decorViewInspector.getFocusedDecorViewInfo(activity) ?: return
            if (topDecorView.decorView == activityDecorView && !activity.isFinishing) {
                logger.d(TAG, "Activity is losing focus")
                showBlurView()
            } else {
                logger.d(TAG, "Activity show dialog")
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
                    logger.d(TAG, "onDecorViewFocusChanged: view=$decorView, focus=$hasFocus")
                    if (hasFocus) {
                        hideBlurView()
                    } else {
                        val topDecorView = decorViewInspector.getFocusedDecorViewInfo(activity) ?: return
                        if (topDecorView.decorView == view) {
                            logger.d(TAG, "Dialog $view is losing focus")
                            showBlurView()
                        } else {
                            logger.d(TAG, "Activity show another dialog $topDecorView")
                            manageDialogEvents(topDecorView)
                        }
                    }
                }
            }
        decorView.viewTreeObserver.addOnWindowFocusChangeListener(focusChangeListener)
        val detachListener =
            object : OnDecorViewDetachListener(decorView) {
                override fun onDecorViewDetached(view: View) {
                    logger.d(TAG, "onDecorViewDetached: $view")
                    view.viewTreeObserver.removeOnWindowFocusChangeListener(focusChangeListener)
                    view.viewTreeObserver.removeOnWindowAttachListener(this)
                    decorViews.removeAll { it.get() == null || it.get() == view }
                }
            }
        decorView.viewTreeObserver.addOnWindowAttachListener(detachListener)
    }

    private fun logWindows() {
        logger.d(
            TAG,
            "Activity: activity=$activity, focus=${activityDecorView.hasWindowFocus()}, decor=$activityDecorView, attrs=${activityDecorView.layoutParams}",
        )
        val decorViews = decorViewInspector.getDecorViewInfos(activity)
        for (view in decorViews) {
            logger.d(
                TAG,
                "Window:   activity=${view.activity}, focus=${view.decorView.hasWindowFocus()}, decor=${view.decorView}, attrs=${view.decorView.layoutParams}",
            )
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        logger.d(TAG, "onResume $owner")
        val viewInfos = decorViewInspector.getDecorViewInfos(activity)
        if (viewInfos.any { it.decorView.hasWindowFocus() }) {
            hideBlurView()
        }
        for (info in viewInfos) {
            if (info.isDialog) {
                manageDialogEvents(info)
            }
        }
        shouldIgnore = false
        instrumentation?.addMonitor(activityMonitor)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        logger.d(TAG, "onPause: $owner")
        if (!activity.isFinishing) {
            showBlurView()
        }
        instrumentation?.removeMonitor(activityMonitor)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        logger.d(TAG, "onDestroy: $owner")
        release()
    }

    private fun showBlurView() {
        logWindows()
        if (!blurStrategy.isShowing && !shouldIgnore) {
            logger.d(TAG, "showBlurView $activity")
            val viewInfo = decorViewInspector.getFocusedDecorViewInfo(activity) ?: return
            try {
                blurStrategy.showBlur(viewInfo)
            } catch (ex: Exception) {
                logger.e(TAG, "Can not show blur view", ex)
            }
        }
    }

    private fun hideBlurView() {
        logWindows()
        if (blurStrategy.isShowing) {
            logger.d(TAG, "hideBlurView $activity")
            try {
                blurStrategy.hideBlur()
            } catch (ex: Exception) {
                logger.e(TAG, "Can not hide blur view", ex)
            }
        }
    }

    override fun onStartActivity(intent: Intent?) {
        shouldIgnore = (intent != null && intentHandler.checkWhiteList(intent))
    }
}
