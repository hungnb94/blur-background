package leoh.screenshot.protector.extension

import android.view.WindowManager.LayoutParams

fun LayoutParams.clone(): LayoutParams {
    val clone = LayoutParams(this.type)
    clone.copyFrom(this)
    return clone
}
