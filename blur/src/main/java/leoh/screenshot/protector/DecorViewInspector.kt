package leoh.screenshot.protector

import android.app.Activity
import android.os.Build
import android.view.ViewGroup
import android.view.inspector.WindowInspector
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.Q)
class DecorViewInspector private constructor() {
    companion object {
        private var instance: DecorViewInspector? = null

        fun getInstance() = instance ?: DecorViewInspector().apply { instance = this }
    }

    fun getDecorViewInfos(activity: Activity): List<DecorViewInfo> {
        val windowViews = WindowInspector.getGlobalWindowViews()
        return windowViews
            .map { DecorViewInfo(it as ViewGroup) }
            .filter { it.activity == activity }
    }

    fun getFocusedDecorViewInfo(activity: Activity): DecorViewInfo? {
        val activityDecorViews = getDecorViewInfos(activity)
        return activityDecorViews.firstOrNull { it.decorView.hasWindowFocus() }
            ?: activityDecorViews.lastOrNull { it.decorView != it.activity?.window?.decorView }
            ?: activityDecorViews.lastOrNull()
    }
}
