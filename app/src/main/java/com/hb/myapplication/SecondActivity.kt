package com.hb.myapplication

import android.os.Bundle

class SecondActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showConfirmationDialog()
        requestOnePermission()
    }
}
