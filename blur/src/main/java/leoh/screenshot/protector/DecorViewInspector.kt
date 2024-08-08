package leoh.screenshot.protector

import android.os.Build
import android.view.inspector.WindowInspector
import androidx.annotation.RequiresApi

class DecorViewInspector private constructor() {
    private val decorViewInfos = mutableListOf<DecorViewInfo>()

    companion object {
        private var instance: DecorViewInspector? = null

        fun getInstance() = instance ?: DecorViewInspector().apply { instance = this }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
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

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getTopDecorViewInfo(): DecorViewInfo? {
        val windowViews = WindowInspector.getGlobalWindowViews()
        for (windowView in windowViews) {
            if (!decorViewInfos.any { it.decorView == windowView }) {
                decorViewInfos.add(DecorViewInfo(windowView))
            }
        }
        decorViewInfos.removeAll { !windowViews.contains(it.decorView) }
        return decorViewInfos.lastOrNull()
    }
}
