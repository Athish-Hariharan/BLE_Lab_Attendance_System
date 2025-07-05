#ifndef SD_LOGGER_H
#define SD_LOGGER_H

#include <SD.h>

void logToSD(String uuid, unsigned long currentMillis) {
  char filename[25];
  sprintf(filename, "/attendance-%04d-%02d-%02d.csv", 2025, 7, 1);  // Replace with RTC time if needed
  File logFile = SD.open(filename, FILE_APPEND);

  if (logFile) {
    logFile.print(uuid);
    logFile.print(",");
    logFile.println(currentMillis);
    logFile.close();
  }
}

#endif
