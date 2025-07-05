#ifndef UUIDCONFIG_H
#define UUIDCONFIG_H

#define MAX_UUIDS 20

String registeredUUIDs[MAX_UUIDS] = {
  "fda50693-a4e2-4fb1-afcf-c6eb07647825"
};

bool isRegistered(String uuid) {
  for (int i = 0; i < MAX_UUIDS; i++) {
    if (registeredUUIDs[i] == uuid) {
      return true;
    }
  }
  return false;
}

#endif
