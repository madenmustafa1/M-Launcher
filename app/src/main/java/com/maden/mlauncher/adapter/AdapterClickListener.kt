package com.maden.mlauncher.adapter

import android.graphics.drawable.Drawable

interface AdapterClickListener {
    fun onClickListener(packageName: String, index: Int, drawable: Drawable)
}

