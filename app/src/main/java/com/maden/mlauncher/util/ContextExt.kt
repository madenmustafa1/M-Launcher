package com.maden.mlauncher.util

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

fun Context.getUserWallpaper(): Bitmap? {
    return try {
        val cw = ContextWrapper(this.applicationContext)
        val directory: File = cw.getDir("photos", AppCompatActivity.MODE_PRIVATE)
        if (!directory.exists()) {
            directory.mkdir()
        }
        val photoPath = File(directory, "wallpaper.png")
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        BitmapFactory.decodeFile(photoPath.path, options)
    } catch (e: Exception) {
        null
    }
}

fun Context.setUserWallpaper(imageBitmap: Bitmap): Boolean {
    return try {
        val cw = ContextWrapper(applicationContext)
        val directory: File = cw.getDir("photos", AppCompatActivity.MODE_PRIVATE)
        if (!directory.exists()) {
            directory.mkdir()
        }
        val myPath = File(directory, "wallpaper.png")
        var fos: FileOutputStream? = null
        fos = FileOutputStream(myPath)
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()
        return false
    } catch (e: Exception) {
        false
    }
}