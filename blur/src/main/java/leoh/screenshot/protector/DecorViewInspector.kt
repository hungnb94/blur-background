package leoh.screenshot.protector

import android.os.Build
import android.view.inspector.WindowInspector
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
                decorViewInfos.add(DecorViewInfo(windowView))
            }
        }
        decorViewInfos.removeAll { !windowViews.contains(it.decorView) }
        return decorViewInfos.toList()
    }

    fun getFocusedDecorViewInfo(): DecorViewInfo? {
        val windowViews = WindowInspector.getGlobalWindowViews()
        for (windowView in windowViews) {
            if (!decorViewInfos.any { it.decorView == windowView }) {
                decorViewInfos.add(DecorViewInfo(windowView))
            }
        }
        decorViewInfos.removeAll { !windowViews.contains(it.decorView) }
        return decorViewInfos.firstOrNull { it.decorView.hasWindowFocus() } ?: decorViewInfos.lastOrNull()
    }
}
