# 📡 BLE Lab Attendance System

An automatic lab attendance system using Bluetooth Low Energy (BLE) beacon advertising from Android phones and ESP32-based BLE scanners on two lab floors.  
It uses a UUID-based presence detection mechanism with a presence timeout to avoid duplicate logging while users move between floors.

---

## 📖 Project Overview

- 📱 **Android App** broadcasts a unique BLE UUID per member.
- 📡 **ESP32 BLE Scanners** on each floor detect nearby UUIDs.
- ⏳ **Presence Timeout** prevents duplicate logs within a defined interval.
- 📂 Attendance logs stored locally on ESP32 SD cards.

---

## 📂 Directory Structure

BLE_Lab_Attendance_System/

├── Android_BLE_Beacon_App/

├── ESP32_BLE_Scanner/

│ ├── Floor_1_Scanner/

│ ├── Floor_2_Scanner/

│ └── common/

├── Attendance_Logs/

├── launch.md

├── README.md

└── LICENSE

---

## 📦 Requirements  

### Android App  
- Android Studio (latest stable)
- Android 5.0+ device with BLE support

### ESP32 Scanner  
- Arduino IDE (with ESP32 board support installed)
- ESP32 BLE Arduino Library
- SD Card Module (FAT32 formatted card)

---

## 🚀 Setup & Launch Instructions  

### 📱 Android App  
1. Install **Android Studio**
2. Open `Android_BLE_Beacon_App/`
3. Build the APK
4. Install on all lab members' phones
5. Turn on Bluetooth and Location on phones
6. Disable battery optimization for the app

---

### 📡 ESP32 BLE Scanner  
1. Install **Arduino IDE**
2. Install **ESP32 board support** and **ESP32 BLE Arduino library**
3. Wire SD Card module:

CS → 5
MOSI → 23
MISO → 19
SCK → 18
VCC → 3.3V
GND → GND

4. Open respective `.ino` file in:
- `ESP32_BLE_Scanner/Floor_1_Scanner/`
- `ESP32_BLE_Scanner/Floor_2_Scanner/`
5. Upload to ESP32
6. Insert SD card
7. Open Serial Monitor to view real-time logs

---

## 📝 How Attendance Logging Works  

- Android app broadcasts a fixed UUID via BLE.
- ESP32 BLE scanners detect advertised UUIDs.
- Each UUID logged once within a timeout window (default: 30 minutes).
- Logs saved to SD card in `Attendance_Logs/attendance-YYYY-MM-DD.csv` format:

---

## 🧪 How to Test  

1. Install and run the Android BLE beacon app.
2. Place ESP32 scanner with SD card inserted.
3. Open Serial Monitor (baud: 115200).
4. Walk into scanner range → UUID logged.
5. Move between floors → No duplicate logs within 30 minutes.
6. Check SD card log files for saved records.

---

## 📈 Future Improvements  

- Central Wi-Fi/MQTT-based attendance log sync
- Web dashboard for reports
- Android app with dynamic UUID per user via Google Form registration

---

## 🙌 Contributing

Pull requests and feature suggestions are welcome.  
Feel free to fork and enhance the project!

---

## 📝 License

This project is open source under the [MIT License](LICENSE).

---

## 👨‍💻 Maintainer

- **Athish**


