package com.bleattendanceapp.data

import android.content.Context
import android.content.SharedPreferences

object UserPreferences {

    private const val PREF_NAME = "BLEAttendancePrefs"
    private const val KEY_IS_REGISTERED = "is_registered"
    private const val KEY_NAME = "user_name"
    private const val KEY_ROLL = "user_roll"
    private const val KEY_UUID = "user_uuid"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isUserRegistered(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_IS_REGISTERED, false)
    }

    fun setUserRegistered(context: Context, registered: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_IS_REGISTERED, registered).apply()
    }

    fun saveUserDetails(context: Context, name: String, roll: String, uuid: String) {
        getPrefs(context).edit()
            .putString(KEY_NAME, name)
            .putString(KEY_ROLL, roll)
            .putString(KEY_UUID, uuid)
            .apply()
    }

    fun getUserName(context: Context): String {
        return getPrefs(context).getString(KEY_NAME, "") ?: ""
    }

    fun getUserRoll(context: Context): String {
        return getPrefs(context).getString(KEY_ROLL, "") ?: ""
    }

    fun getUserUUID(context: Context): String {
        return getPrefs(context).getString(KEY_UUID, "") ?: ""
    }
}