package com.hb.myapplication

import android.Manifest
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import leoh.screenshot.protector.IScreenshotProtector
import leoh.screenshot.protector.ScreenshotProtector
import leoh.screenshot.protector.SimpleScreenshotProtector

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var screenshotProtector: IScreenshotProtector
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        screenshotProtector =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ScreenshotProtector(this)
            } else {
                SimpleScreenshotProtector(this)
            }
        screenshotProtector.protect()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        textView = findViewById(R.id.tvMessage)
        textView.setOnClickListener {
            showConfirmationDialog()
//            startActivity(Intent(this, MainActivity::class.java))
        }
        showConfirmationDialog()
        requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
    }

    private fun showConfirmationDialog() {
        ConfirmationDialogFragment().show(supportFragmentManager, ConfirmationDialogFragment.TAG)
    }
}

class ConfirmationDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog
            .Builder(requireContext())
            .setMessage("This is message")
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .create()

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}
