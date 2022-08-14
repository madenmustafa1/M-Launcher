package com.maden.mlauncher.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPrefsManager(context: Context) {
    private var _packageName = "com.maden.mlauncher.util"
    private var _sharedPrefs: SharedPreferences =
        context.getSharedPreferences(_packageName, Context.MODE_PRIVATE)

    val firstApp = "FIRST_APP"
    val secondApp = "SECOND_APP"
    val thirdApp = "THIRD_APP"

    fun <T> setData(key: String, data: T) {
        _sharedPrefs.edit(commit = true) {
            if (data is Boolean) putBoolean(key, data)
            if (data is String) putString(key, data)
            if (data is Int) putInt(key, data)
        }
    }
    fun getString(key: String) = _sharedPrefs.getString(key, null)
    fun getInt(key: String, defaultValue: Int) = _sharedPrefs.getInt(key, defaultValue)
    fun getBoolean(key: String, defaultValue: Boolean) = _sharedPrefs.getBoolean(key, defaultValue)
}