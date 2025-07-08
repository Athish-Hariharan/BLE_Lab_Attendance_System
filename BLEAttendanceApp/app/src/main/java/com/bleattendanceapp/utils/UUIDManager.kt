package com.bleattendance.utils

import android.content.Context
import java.util.*

object UUIDManager {

    private const val PREF_NAME = "attendance_prefs"
    private const val KEY_UUID = "device_uuid"

    fun getOrCreateUUID(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        var uuid = prefs.getString(KEY_UUID, null)

        if (uuid == null) {
            uuid = UUID.randomUUID().toString()
            prefs.edit().putString(KEY_UUID, uuid).apply()
        }
        return uuid
    }
}
