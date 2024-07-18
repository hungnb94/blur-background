package com.hb.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hb.blur.BlurActivity
import com.hb.blur.ScreenshotProtector

private const val TAG = "MainActivity"

class MainActivity : BlurActivity() {
    private lateinit var textView: TextView
    private lateinit var screenshotProtector: ScreenshotProtector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        screenshotProtector = ScreenshotProtector(this)
//        screenshotProtector.startProtect()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textView = findViewById(R.id.tvMessage)
    }

    override fun onDestroy() {
        super.onDestroy()
//        screenshotProtector.stopProtect()
    }
}
