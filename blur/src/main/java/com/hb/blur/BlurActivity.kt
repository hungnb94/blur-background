package com.hb.blur

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "BlurActivity"

abstract class BlurActivity : AppCompatActivity() {
    private var isActivityFocus = false
    private var mContentView: ViewGroup? = null

    private fun getContentView(): ViewGroup =
        mContentView
            ?: findViewById<ViewGroup>(android.R.id.content).apply { mContentView = this }

    private var _blurView: View? = null

    open fun getBlurView(): View =
        _blurView
            ?: View(this).apply {
                _blurView = this
            }

    override fun onResume() {
        super.onResume()
        setActivityFocus(true)
    }

    override fun onPause() {
        super.onPause()
        setActivityFocus(false)
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        super.onTopResumedActivityChanged(isTopResumedActivity)
        setActivityFocus(isTopResumedActivity)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        setActivityFocus(hasFocus)
    }

    private fun setActivityFocus(hasFocus: Boolean) {
        if (isActivityFocus != hasFocus) {
            isActivityFocus = hasFocus
            onActivityFocusChanged(hasFocus)
        }
    }

    open fun onActivityFocusChanged(hasFocus: Boolean) {
        Log.d(TAG, "onActivityFocusChanged: $hasFocus")
        if (hasFocus) {
            hideBlurView()
        } else {
            showBlurView()
        }
    }

    private fun showBlurView() {
        val blurView = getBlurView()
        if (blurView.parent == null) {
            getContentView().addView(
                blurView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            if (isNightMode) {
                blurView.setBackgroundColor(Color.BLACK)
            } else {
                blurView.setBackgroundColor(Color.WHITE)
            }
        }
    }

    private fun hideBlurView() {
        getContentView().removeView(getBlurView())
    }
}
