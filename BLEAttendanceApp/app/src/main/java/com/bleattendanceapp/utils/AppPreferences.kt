package com.bleattendance.utils

import android.content.Context

object AppPreferences {

    private const val PREF_NAME = "attendance_prefs"

    fun saveString(context: Context, key: String, value: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(key, value).apply()
    }

    fun getString(context: Context, key: String): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(key, null)
    }
}
