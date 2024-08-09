package leoh.screenshot.protector

import android.os.Build
import android.view.ViewGroup
import android.view.inspector.WindowInspector
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.Q)
class DecorViewInspector private constructor() {
    private val decorViewInfos = mutableListOf<DecorViewInfo>()

    companion object {
        private var instance: DecorViewInspector? = null

        fun getInstance() = instance ?: DecorViewInspector().apply { instance = this }
    }

    fun getDecorViewInfos(): List<DecorViewInfo> {
        val windowViews = WindowInspector.getGlobalWindowViews()
        for (windowView in windowViews) {
            if (!decorViewInfos.any { it.decorView == windowView }) {
                decorViewInfos.add(DecorViewInfo(windowView as ViewGroup))
            }
        }
        decorViewInfos.removeAll { !windowViews.contains(it.decorView) }
        return decorViewInfos.toList()
    }

    fun getFocusedDecorViewInfo(activity: ComponentActivity): DecorViewInfo? {
        getDecorViewInfos()
        val activityDecorViews = decorViewInfos.filter { it.activity == activity }
        return activityDecorViews.firstOrNull { it.decorView.hasWindowFocus() }
            ?: activityDecorViews.lastOrNull { it.decorView != it.activity?.window?.decorView }
            ?: activityDecorViews.lastOrNull()
    }
}
