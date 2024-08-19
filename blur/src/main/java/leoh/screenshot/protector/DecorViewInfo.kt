package leoh.screenshot.protector

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION
import android.view.WindowManager.LayoutParams.TYPE_TOAST
import leoh.screenshot.protector.extension.activity

data class DecorViewInfo(
    val decorView: View,
) {
    private val contentView: ViewGroup? by lazy { decorView.findViewById(android.R.id.content) }
    val activity: Activity? by lazy { decorView.activity ?: contentView?.activity }
    val isToast: Boolean
        get() = decorView.layoutParams is LayoutParams && (decorView.layoutParams as LayoutParams).type == TYPE_TOAST
    val isDialog: Boolean
        get() =
            activity != null &&
                decorView != activity?.window?.decorView &&
                decorView.layoutParams is LayoutParams &&
                (decorView.layoutParams as LayoutParams).type == TYPE_APPLICATION
}
