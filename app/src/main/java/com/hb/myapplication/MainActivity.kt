package com.hb.myapplication

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.dmoral.toasty.Toasty
import leoh.screenshot.protector.ScreenshotProtector
import leoh.screenshot.protector.extension.isNightMode
import java.util.Random

open class MainActivity : AppCompatActivity() {
    private lateinit var btnShowDialog: Button
    private lateinit var btnShowTwoDialogs: Button
    private lateinit var btnToastMessage: Button
    private lateinit var btnRequestPermission: Button
    private lateinit var btnEnableBluetooth: Button
    private lateinit var btnStartActivityDialog: Button
    private lateinit var btnStartActivityDialogPermission: Button
    private lateinit var container: ViewGroup
    private val mainHandler = Handler(Looper.getMainLooper())

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
        btnShowTwoDialogs = findViewById(R.id.btnShowTwoDialogs)
        btnToastMessage = findViewById(R.id.btnToastMessage)
        btnRequestPermission = findViewById(R.id.btnRequestPermission)
        btnEnableBluetooth = findViewById(R.id.btnEnableBluetooth)
        btnStartActivityDialog = findViewById(R.id.btnStartActivityDialog)
        btnStartActivityDialogPermission = findViewById(R.id.btnStartActivityDialogPermission)
        btnShowDialog.setOnClickListener {
            showConfirmationDialog()
        }
        btnShowTwoDialogs.setOnClickListener {
            showTwoDialogs()
        }
        btnToastMessage.setOnClickListener {
            Toasty.info(this, "Hello world", Toasty.LENGTH_LONG).show()
        }
        btnRequestPermission.setOnClickListener {
            requestOnePermission()
        }
        btnEnableBluetooth.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, 1)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 0)
            }
        }
        btnStartActivityDialog.setOnClickListener {
            btnStartActivityDialog.postDelayed({
                startActivity(SecondActivity.newIntent(this, showDialog = true, requestPermission = false))
                finish()
            }, 2000)
        }
        btnStartActivityDialogPermission.setOnClickListener {
            btnStartActivityDialogPermission.postDelayed({
                startActivity(SecondActivity.newIntent(this, showDialog = true, requestPermission = true))
                finish()
            }, 2000)
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

    private fun showTwoDialogs() {
        Log.d(TAG, "show first dialog")
        showConfirmationDialog()
        mainHandler.postDelayed({
            Log.d(TAG, "show second dialog")
            showConfirmationDialog()
        }, 1000)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
