package com.hb.myapplication

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.dmoral.toasty.Toasty
import leoh.screenshot.protector.ScreenshotProtector
import leoh.screenshot.protector.extension.isNightMode
import java.util.Random

open class MainActivity : AppCompatActivity() {
    private lateinit var btnShowDialog: Button
    private lateinit var btnToastMessage: Button
    private lateinit var btnRequestPermission: Button
    private lateinit var btnStartActivity: Button
    private lateinit var container: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ScreenshotProtector.protect(this)
        setContentView(R.layout.activity_main)
        container = findViewById(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(container) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        container.setBackgroundColor(randomBackgroundColor())
        btnShowDialog = findViewById(R.id.btnShowDialog)
        btnToastMessage = findViewById(R.id.btnToastMessage)
        btnRequestPermission = findViewById(R.id.btnRequestPermission)
        btnStartActivity = findViewById(R.id.btnStartActivity)
        btnShowDialog.setOnClickListener {
            showConfirmationDialog()
        }
        btnToastMessage.setOnClickListener {
            Toasty.info(this, "Hello world", Toasty.LENGTH_LONG).show()
        }
        btnRequestPermission.setOnClickListener {
            requestOnePermission()
        }
        btnStartActivity.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
            finish()
        }
    }

    private fun randomBackgroundColor(): Int {
        val rnd = Random()
        val range = 120
        if (isNightMode) {
            return Color.rgb(rnd.nextInt(range), rnd.nextInt(range), rnd.nextInt(range))
        } else {
            val base = 255 - range
            return Color.rgb(
                base + rnd.nextInt(range),
                base + rnd.nextInt(range),
                base + rnd.nextInt(range),
            )
        }
    }

    fun requestOnePermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
    }

    fun showConfirmationDialog() {
        ConfirmationDialogFragment().show(supportFragmentManager, ConfirmationDialogFragment.TAG)
    }
}
