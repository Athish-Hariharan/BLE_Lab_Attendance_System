package com.bleattendanceapp

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.bleattendanceapp.screens.AttendanceScreen
import com.bleattendanceapp.screens.DashboardScreen
import com.bleattendanceapp.screens.RegisterScreen
import com.bleattendanceapp.data.UserPreferences
import com.bleattendanceapp.ui.theme.BLEAttendanceAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BLEAttendanceAppTheme {
                var isRegistered by remember {
                    mutableStateOf(UserPreferences.isUserRegistered(this))
                }
                var onAttendanceScreen by remember { mutableStateOf(false) }

                val context = LocalContext.current

                // Permission launcher for Bluetooth Advertise (Android 12+)
                val advertisePermissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted) {
                        onAttendanceScreen = true
                    } else {
                        Toast.makeText(
                            context,
                            "Bluetooth Advertise permission denied.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                when {
                    !isRegistered -> {
                        RegisterScreen(
                            onRegisterSuccess = {
                                UserPreferences.setUserRegistered(this, true)
                                isRegistered = true
                            }
                        )
                    }

                    onAttendanceScreen -> {
                        AttendanceScreen(
                            onBack = {
                                onAttendanceScreen = false
                            }
                        )
                    }

                    else -> {
                        DashboardScreen(
                            onStartAttendance = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    if (checkSelfPermission(Manifest.permission.BLUETOOTH_ADVERTISE)
                                        == android.content.pm.PackageManager.PERMISSION_GRANTED
                                    ) {
                                        onAttendanceScreen = true
                                    } else {
                                        advertisePermissionLauncher.launch(Manifest.permission.BLUETOOTH_ADVERTISE)
                                    }
                                } else {
                                    onAttendanceScreen = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
