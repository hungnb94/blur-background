package com.hb.myapplication

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ConfirmationDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog
            .Builder(requireContext())
            .setMessage("Dialog ${++count}")
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .create()

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
        private var count = 0
    }
}
