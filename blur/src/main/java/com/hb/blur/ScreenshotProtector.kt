package com.hb.blur

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

private const val TAG = "ScreenshotProtector"

class ScreenshotProtector(
    private val activity: Activity,
) : ViewTreeObserver.OnWindowFocusChangeListener {
    private val contentView: ViewGroup = activity.findViewById(android.R.id.content)
    private val blurView = View(activity)

    fun startProtect() {
        contentView.viewTreeObserver.addOnWindowFocusChangeListener(this)
    }

    fun stopProtect() {
        contentView.viewTreeObserver.removeOnWindowFocusChangeListener(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        Log.d(TAG, "onWindowFocusChanged: $hasFocus")
        if (hasFocus) {
            hideBlurView()
        } else {
            showBlurView()
        }
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
        contentView.removeView(blurView)
    }
}

val Context.isNightMode: Boolean
    get() {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
