package leoh.screenshot.protector

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View

val Context.activity: Activity?
    get() {
        var context: Context? = this
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

val View.activity: Activity?
    get() = context.activity
