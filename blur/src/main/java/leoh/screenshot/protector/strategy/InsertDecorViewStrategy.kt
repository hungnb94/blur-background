package leoh.screenshot.protector.strategy

import android.app.Activity
import leoh.screenshot.protector.DecorViewInfo

class InsertDecorViewStrategy(
    activity: Activity,
) : BlurStrategy(activity) {
    override fun showBlur(viewInfo: DecorViewInfo) {
    }

    override fun hideBlur() {
    }
}
