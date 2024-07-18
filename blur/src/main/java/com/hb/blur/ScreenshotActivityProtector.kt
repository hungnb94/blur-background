package com.hb.blur

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

private const val TAG = "ScreenshotProtector"

internal class ScreenshotActivityProtector(
    private val activity: ComponentActivity,
) : ViewTreeObserver.OnWindowFocusChangeListener,
    DefaultLifecycleObserver {
    private val contentView: ViewGroup = activity.findViewById(android.R.id.content)
    private val blurView = View(activity)

    fun protect() {
        contentView.viewTreeObserver.addOnWindowFocusChangeListener(this)
        activity.lifecycle.addObserver(this)
    }

    private fun release() {
        contentView.viewTreeObserver.removeOnWindowFocusChangeListener(this)
        activity.lifecycle.removeObserver(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            hideBlurView()
        } else {
            showBlurView()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        release()
    }

    private fun showBlurView() {
        if (blurView.parent == null) {
            contentView.addView(
                blurView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            if (activity.isNightMode) {
                blurView.setBackgroundColor(Color.BLACK)
            } else {
                blurView.setBackgroundColor(Color.WHITE)
            }
        }
    }

    private fun hideBlurView() {
        if (blurView.parent != null) {
            contentView.removeView(blurView)
        }
    }
}

val Context.isNightMode: Boolean
    get() {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
