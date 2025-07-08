package com.bleattendanceapp.ble

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.ParcelUuid
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bleattendanceapp.R

class BLEAdvertiserService : Service() {

    private val TAG = "BLEAdvertiserService"
    private var advertiser: BluetoothLeAdvertiser? = null
    private var advertiseCallback: AdvertiseCallback? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val uuidString = intent?.getStringExtra("EXTRA_UUID")
        if (uuidString.isNullOrEmpty()) {
            Log.e(TAG, "No UUID provided — stopping service")
            stopSelf()
            return START_NOT_STICKY
        }

        startForegroundService()
        startAdvertising(uuidString)

        return START_STICKY
    }

    private fun startAdvertising(uuidString: String) {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            Log.e(TAG, "Bluetooth is not enabled — stopping service")
            stopSelf()
            return
        }

        advertiser = bluetoothAdapter.bluetoothLeAdvertiser
        if (advertiser == null) {
            Log.e(TAG, "Device does not support BLE advertising — stopping service")
            stopSelf()
            return
        }

        // Runtime permission check for Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val hasAdvertisePermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_ADVERTISE
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasAdvertisePermission) {
                Log.e(TAG, "Missing BLUETOOTH_ADVERTISE permission — stopping service")
                stopSelf()
                return
            }
        }

        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setConnectable(false)
            .build()

        val data = AdvertiseData.Builder()
            .addServiceUuid(ParcelUuid.fromString(uuidString))
            .setIncludeDeviceName(true)
            .build()

        advertiseCallback = object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                Log.i(TAG, "Advertising started successfully")
            }

            override fun onStartFailure(errorCode: Int) {
                Log.e(TAG, "Advertising failed with error code: $errorCode")
                stopSelf()
            }
        }

        advertiser?.startAdvertising(settings, data, advertiseCallback)
    }

    private fun startForegroundService() {
        val channelId = "BLE_ADVERTISER_CHANNEL"
        val channelName = "BLE Attendance Advertising"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("BLE Attendance Advertising Active")
            .setContentText("Your device is now advertising for attendance.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(101, notification)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (advertiser != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val hasAdvertisePermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_ADVERTISE
            ) == PackageManager.PERMISSION_GRANTED

            if (hasAdvertisePermission) {
                advertiser?.stopAdvertising(advertiseCallback)
                Log.i(TAG, "BLE Advertising stopped")
            } else {
                Log.w(TAG, "No permission to stop advertising — skipping stopAdvertising()")
            }
        } else {
            // On pre-Android 12, stopAdvertising() didn’t need explicit permission
            advertiser?.stopAdvertising(advertiseCallback)
            Log.i(TAG, "BLE Advertising stopped (pre-Android 12 or no advertiser)")
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
