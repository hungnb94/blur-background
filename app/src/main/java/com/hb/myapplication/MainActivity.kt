package com.hb.myapplication

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import leoh.screenshot.protector.ScreenshotProtector

open class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ScreenshotProtector.protect(this)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textView = findViewById(R.id.tvMessage)
        textView.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }

    fun requestOnePermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
    }

    fun showConfirmationDialog() {
        ConfirmationDialogFragment().show(supportFragmentManager, ConfirmationDialogFragment.TAG)
    }
}
