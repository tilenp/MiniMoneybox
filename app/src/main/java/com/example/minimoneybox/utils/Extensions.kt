package com.example.minimoneybox.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import com.example.minimoneybox.R

fun Activity.setOrientationForDeviceType() {
    requestedOrientation = if (resources.getBoolean(R.bool.portrait_only)) {
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    } else {
        ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}