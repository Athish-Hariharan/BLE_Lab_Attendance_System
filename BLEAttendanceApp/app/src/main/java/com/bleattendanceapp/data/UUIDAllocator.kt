package com.bleattendanceapp.data

import android.content.Context
import java.util.*

object UUIDAllocator {
    private const val UUID_PREF = "uuid_pool"
    private const val USED_UUIDS_KEY = "used_uuids"

    fun assignUUID(context: Context): String {
        val prefs = context.getSharedPreferences(UUID_PREF, Context.MODE_PRIVATE)
        val used = prefs.getStringSet(USED_UUIDS_KEY, mutableSetOf()) ?: mutableSetOf()
        var uuid: String

        do {
            uuid = UUID.randomUUID().toString()
        } while (used.contains(uuid))

        used.add(uuid)
        prefs.edit().putStringSet(USED_UUIDS_KEY, used).apply()

        return uuid
    }
}
