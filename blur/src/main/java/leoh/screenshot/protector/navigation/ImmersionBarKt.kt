package leoh.screenshot.protector.navigation

import android.content.Context

/**
 * copy from https://github.com/gyf-dev/ImmersionBar
 */

val Context.isGesture get() = GestureUtils.getGestureBean(this).isGesture
