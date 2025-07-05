# BLE Lab Attendance System — Setup & Launch Guide

## Android App Setup:
1. Install Android Studio
2. Open Android_BLE_Beacon_App/
3. Build and install APK on member phones
4. Ensure Bluetooth and Location permissions granted
5. Disable battery optimization for the app

## ESP32 BLE Scanner Setup:
1. Install Arduino IDE
2. Install ESP32 board package
3. Install ESP32 BLE Arduino library
4. Connect ESP32 + SD Card module (CS=5)
5. Open respective scanner sketch in Floor_1_Scanner/ or Floor_2_Scanner/
6. Upload to ESP32 board
7. Insert SD card (FAT32)
8. Open Serial Monitor for live logs

## Attendance Logging:
- Logs saved in /Attendance_Logs on SD card per ESP32
- File: attendance-YYYY-MM-DD.csv

## Directory Structure:
Refer to project tree in README.md

## Notes:
- Duplicate entries within 30 mins are ignored via PresenceManager
- UUIDs managed centrally in UUIDConfig.h
- Test scanning via nRF Connect app on mobile

## Contact
Maintainer: Athish Hariharan
