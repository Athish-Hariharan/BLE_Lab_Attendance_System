package com.bleattendance

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.ParcelUuid
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import java.util.*

class MainActivity : ComponentActivity() {

    private var bluetoothLeAdvertiser: BluetoothLeAdvertiser? = null
    private val PREF_UUID_KEY = "advertiser_uuid"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check Bluetooth support
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            Log.e("BLE", "Bluetooth not supported or disabled")
            return
        }

        // Check permissions
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_ADVERTISE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 1
            )
        }

        bluetoothLeAdvertiser = bluetoothAdapter.bluetoothLeAdvertiser

        // Compose UI
        setContent {
            MaterialTheme {
                Surface {
                    UUIDInputScreen()
                }
            }
        }
    }

    @Composable
    fun UUIDInputScreen() {
        val prefs = getSharedPreferences("ble_attendance_prefs", Context.MODE_PRIVATE)
        var uuidText by remember { mutableStateOf(prefs.getString(PREF_UUID_KEY, "") ?: "") }

        Column(modifier = Modifier.padding(24.dp)) {
            Text("Enter BLE Beacon UUID")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = uuidText,
                onValueChange = { uuidText = it },
                label = { Text("UUID") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    prefs.edit {
                        putString(PREF_UUID_KEY, uuidText)
                    }
                    startAdvertising(uuidText)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Advertising")
            }
        }
    }

    private fun startAdvertising(uuidStr: String) {
        try {
            val uuid = UUID.fromString(uuidStr)

            val settings = AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setConnectable(false)
                .setTimeout(0)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .build()

            val data = AdvertiseData.Builder()
                .setIncludeDeviceName(true)
                .addServiceUuid(ParcelUuid(uuid))
                .build()

            bluetoothLeAdvertiser?.startAdvertising(settings, data, advertiseCallback)
        } catch (e: IllegalArgumentException) {
            Log.e("BLE", "Invalid UUID format: ${e.localizedMessage}")
        }
    }

    private val advertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            super.onStartSuccess(settingsInEffect)
            Log.i("BLE", "Advertising started successfully")
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)
            Log.e("BLE", "Advertising failed: $errorCode")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothLeAdvertiser?.stopAdvertising(advertiseCallback)
    }
}
