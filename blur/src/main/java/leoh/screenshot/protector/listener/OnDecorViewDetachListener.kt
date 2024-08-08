package leoh.screenshot.protector.listener

import android.view.View
import android.view.ViewTreeObserver.OnWindowAttachListener

private const val TAG = "OnWindowDetachListener"

abstract class OnDecorViewDetachListener(
    private val decorView: View,
) : OnWindowAttachListener {
    override fun onWindowAttached() {
        // do nothing
    }

    override fun onWindowDetached() {
        onDecorViewDetached(view = decorView)
    }

    abstract fun onDecorViewDetached(view: View)
}
