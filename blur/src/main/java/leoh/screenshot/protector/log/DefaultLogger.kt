package leoh.screenshot.protector.log

import android.util.Log

internal class DefaultLogger : Logger {
    override fun d(
        tag: String,
        msg: String,
        throwable: Throwable?,
    ) {
        Log.d(tag, msg, throwable)
    }

    override fun i(
        tag: String,
        msg: String,
        throwable: Throwable?,
    ) {
        Log.i(tag, msg, throwable)
    }

    override fun w(
        tag: String,
        msg: String,
        throwable: Throwable?,
    ) {
        Log.w(tag, msg, throwable)
    }

    override fun e(
        tag: String,
        msg: String,
        throwable: Throwable?,
    ) {
        Log.e(tag, msg, throwable)
    }
}
