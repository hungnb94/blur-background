package leoh.screenshot.protector

import android.graphics.drawable.Drawable
import android.view.View
import android.view.WindowManager

class SourceDecorViewInfo(
    val decorView: View,
    val layoutParams: WindowManager.LayoutParams,
    val background: Drawable?,
)
