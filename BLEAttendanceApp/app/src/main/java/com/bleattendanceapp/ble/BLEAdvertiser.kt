package com.bleattendanceapp.ble

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.os.ParcelUuid
import android.util.Log
import java.util.UUID

class BleAdvertiser(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private var advertiser: BluetoothLeAdvertiser? = null

    private val advertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            super.onStartSuccess(settingsInEffect)
            Log.d("BLEAdvertiser", "Advertising started successfully")
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)
            Log.e("BLEAdvertiser", "Advertising failed to start: $errorCode")
        }
    }

    fun startAdvertising(uuid: UUID) {
        if (!bluetoothAdapter.isEnabled) {
            Log.e("BLEAdvertiser", "Bluetooth is not enabled")
            return
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val permission = android.Manifest.permission.BLUETOOTH_ADVERTISE
            val granted = androidx.core.content.ContextCompat.checkSelfPermission(
                context, permission
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED

            if (!granted) {
                Log.e("BLEAdvertiser", "Missing BLUETOOTH_ADVERTISE permission")
                return
            }
        }

        advertiser = bluetoothAdapter.bluetoothLeAdvertiser

        if (advertiser == null) {
            Log.e("BLEAdvertiser", "BluetoothLeAdvertiser is null")
            return
        }

        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setConnectable(false)
            .setTimeout(0)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .build()

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(false)
            .addServiceUuid(ParcelUuid(uuid))
            .build()

        advertiser?.startAdvertising(settings, data, advertiseCallback)
        Log.d("BLEAdvertiser", "Started advertising UUID: $uuid")
    }

    fun stopAdvertising() {
        if (advertiser == null) {
            Log.d("BLEAdvertiser", "No active advertiser to stop")
            return
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val permission = Manifest.permission.BLUETOOTH_ADVERTISE
            val granted = ContextCompat.checkSelfPermission(
                context, permission
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                Log.e("BLEAdvertiser", "Missing BLUETOOTH_ADVERTISE permission, cannot stop advertising")
                return
            }
        }

        advertiser?.stopAdvertising(advertiseCallback)
        Log.d("BLEAdvertiser", "Stopped advertising")
    }

}
