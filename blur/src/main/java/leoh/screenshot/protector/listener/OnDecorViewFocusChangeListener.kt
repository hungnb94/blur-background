package leoh.screenshot.protector.listener

import android.view.View
import android.view.ViewTreeObserver.OnWindowFocusChangeListener

abstract class OnDecorViewFocusChangeListener(
    private val decorView: View,
) : OnWindowFocusChangeListener {
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        onDecorViewFocusChanged(decorView, hasFocus)
    }

    abstract fun onDecorViewFocusChanged(
        view: View,
        hasFocus: Boolean,
    )
}
