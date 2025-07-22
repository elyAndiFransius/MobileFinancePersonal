package com.example.personalfinancemobile.app.ui.utils

import android.app.Activity
import android.widget.ImageView

fun setupBackButton(activity: Activity, backButton: ImageView) {
    backButton.setOnClickListener {
        activity.finish() // kembali ke Activity sebelumnya
    }
}