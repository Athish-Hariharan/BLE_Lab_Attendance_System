#ifndef PRESENCEMANAGER_H
#define PRESENCEMANAGER_H

struct DevicePresence {
  String uuid;
  unsigned long lastSeen;
};

DevicePresence presenceList[50];
int listSize = 0;

bool shouldLog(String uuid, unsigned long currentMillis, unsigned long timeout = 1800000) {
  for (int i = 0; i < listSize; i++) {
    if (presenceList[i].uuid == uuid) {
      if (currentMillis - presenceList[i].lastSeen < timeout) {
        return false;
      } else {
        presenceList[i].lastSeen = currentMillis;
        return true;
      }
    }
  }
  presenceList[listSize++] = {uuid, currentMillis};
  return true;
}

#endif
