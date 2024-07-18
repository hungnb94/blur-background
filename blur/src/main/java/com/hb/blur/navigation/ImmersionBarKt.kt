package com.hb.blur.navigation

import android.content.Context

/**
 * copy from https://github.com/gyf-dev/ImmersionBar
 */

internal val Context.isGesture get() = GestureUtils.getGestureBean(this).isGesture
