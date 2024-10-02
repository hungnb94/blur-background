package leoh.screenshot.protector.log

interface Logger {
    fun d(
        tag: String,
        msg: String,
        throwable: Throwable? = null,
    )

    fun i(
        tag: String,
        msg: String,
        throwable: Throwable? = null,
    )

    fun w(
        tag: String,
        msg: String,
        throwable: Throwable? = null,
    )

    fun e(
        tag: String,
        msg: String,
        throwable: Throwable? = null,
    )
}
