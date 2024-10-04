package com.hb.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle

class SecondActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extras = intent.extras
        if (extras?.getBoolean(KEY_SHOW_DIALOG, false) == true) {
            showConfirmationDialog()
        }
        if (extras?.getBoolean(KEY_REQUEST_PERMISSION, false) == true) {
            requestOnePermission()
        }
    }

    companion object {
        const val KEY_SHOW_DIALOG = "dialog"
        const val KEY_REQUEST_PERMISSION = "permission"

        fun newIntent(
            context: Context,
            showDialog: Boolean = false,
            requestPermission: Boolean = false,
        ): Intent =
            Intent(context, SecondActivity::class.java).apply {
                val extras = Bundle()
                extras.putBoolean(KEY_SHOW_DIALOG, showDialog)
                extras.putBoolean(KEY_REQUEST_PERMISSION, requestPermission)
                putExtras(extras)
            }
    }
}
