package com.bleattendance.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object BLEPermissionManager {

    private const val REQUEST_CODE = 1001

    private val permissions = arrayOf(
        android.Manifest.permission.BLUETOOTH,
        android.Manifest.permission.BLUETOOTH_ADMIN,
        android.Manifest.permission.BLUETOOTH_ADVERTISE,
        android.Manifest.permission.BLUETOOTH_SCAN,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun hasPermissions(context: Context): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE)
    }

    fun handlePermissionsResult(activity: Activity, requestCode: Int, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(activity, "Permissions granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Permissions denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
