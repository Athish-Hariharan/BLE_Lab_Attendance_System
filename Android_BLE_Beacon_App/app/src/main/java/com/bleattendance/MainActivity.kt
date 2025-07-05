package com.bleattendance

import android.app.*
import android.bluetooth.le.*
import android.os.*
import android.bluetooth.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var advertiser: BluetoothLeAdvertiser
    private lateinit var advertiseCallback: AdvertiseCallback

    private val uuid = UUID.fromString("fda50693-a4e2-4fb1-afcf-c6eb07647825") // Replace per person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        advertiser = bluetoothAdapter.bluetoothLeAdvertiser

        val data = AdvertiseData.Builder()
            .addServiceUuid(ParcelUuid(uuid))
            .setIncludeDeviceName(false)
            .build()

        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
            .setConnectable(false)
            .build()

        advertiseCallback = object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                super.onStartSuccess(settingsInEffect)
            }
            override fun onStartFailure(errorCode: Int) {
                super.onStartFailure(errorCode)
            }
        }

        advertiser.startAdvertising(settings, data, advertiseCallback)
    }
}
