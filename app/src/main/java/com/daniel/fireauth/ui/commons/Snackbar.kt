package com.daniel.fireauth.ui.commons

import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar

class Snackbar {
    companion object {
        fun showMessage(message: String, layout: ConstraintLayout) {
            val snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT)
            snackbar.show()
            val snackbarView = snackbar.view
            snackbarView.setBackgroundColor(Color.parseColor("#2c384a"))
            snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        }
    }

}