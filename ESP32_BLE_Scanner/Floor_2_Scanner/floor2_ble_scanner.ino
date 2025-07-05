#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEScan.h>
#include <SPI.h>
#include <SD.h>

#include "../common/UUIDConfig.h"
#include "../common/PresenceManager.h"
#include "../common/SD_Logger.h"

BLEScan* pBLEScan;

void setup() {
  Serial.begin(115200);
  SD.begin(5);
  BLEDevice::init("Floor 2 Scanner");
  pBLEScan = BLEDevice::getScan();
  pBLEScan->setActiveScan(true);
  pBLEScan->setInterval(1000);
  pBLEScan->setWindow(500);
}

void loop() {
  BLEScanResults foundDevices = pBLEScan->start(5, false);
  unsigned long currentMillis = millis();

  for (int i = 0; i < foundDevices.getCount(); i++) {
    BLEAdvertisedDevice device = foundDevices.getDevice(i);
    String uuid = device.getServiceUUID().toString().c_str();

    if (isRegistered(uuid)) {
      if (shouldLog(uuid, currentMillis, 1800000)) {
        logToSD(uuid, currentMillis);
      }
    }
  }
  pBLEScan->clearResults();
}
