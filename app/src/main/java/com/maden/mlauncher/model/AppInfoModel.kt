package com.maden.mlauncher.model

import android.graphics.drawable.Drawable

data class AppInfoModel(
    var label: CharSequence,
    var packageName: CharSequence,
    var icon: Drawable,
)
