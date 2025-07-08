package com.bleattendanceapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.bleattendanceapp.ui.components.StatCard
import com.bleattendanceapp.ui.components.PrimaryButton
import com.bleattendanceapp.data.UserPreferences
import com.bleattendanceapp.ble.BLEAdvertiserService

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val name = remember { UserPreferences.getUserName(context) }
    val rollNumber = remember { UserPreferences.getUserRoll(context) }

    // Launcher to request BLUETOOTH_ADVERTISE permission on Android 12+
    val advertisePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startAdvertisingService(context)
        } else {
            Toast.makeText(context, "Permission required for BLE Advertising", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome, $name!",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Roll Number: $rollNumber",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Example stat cards (placeholder)
        StatCard(title = "Total Sessions Present", value = "0")
        Spacer(modifier = Modifier.height(16.dp))
        StatCard(title = "Current Lab", value = "None")

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = "Start Attendance Advertising",
            onClick = {
                val uuid = UserPreferences.getUserUUID(context)
                if (uuid.isNullOrEmpty()) {
                    Toast.makeText(context, "UUID not set — please register first", Toast.LENGTH_SHORT).show()
                    return@PrimaryButton
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_ADVERTISE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        startAdvertisingService(context)
                    } else {
                        advertisePermissionLauncher.launch(Manifest.permission.BLUETOOTH_ADVERTISE)
                    }
                } else {
                    startAdvertisingService(context)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun startAdvertisingService(context: android.content.Context) {
    val uuid = UserPreferences.getUserUUID(context)
    val intent = Intent(context, BLEAdvertiserService::class.java).apply {
        putExtra("EXTRA_UUID", uuid)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
}
