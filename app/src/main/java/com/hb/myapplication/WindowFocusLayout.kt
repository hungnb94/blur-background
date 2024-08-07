package com.hb.myapplication

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout

private const val TAG = "BlurLayout"

class WindowFocusLayout
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
    ) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
        override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
            super.onWindowFocusChanged(hasWindowFocus)
            Log.d(TAG, "onWindowFocusChanged: $hasWindowFocus")
            invalidate()
        }

        override fun dispatchDraw(canvas: Canvas) {
            Log.d(TAG, "dispatchDraw: $canvas")
            if (hasWindowFocus()) {
                super.dispatchDraw(canvas)
            } else {
                // do nothing
            }
        }
    }
