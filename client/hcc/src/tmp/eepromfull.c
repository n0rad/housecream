#define CONFIG_VERSION "hcc"
#define CONFIG_START 0

struct WiFiStorageStruct {
  char version[4];
  char ssid[24];
  char pwd[16];
  byte addr[4];
  unsigned int id;
} WiFiConfig = {
  CONFIG_VERSION,
  "NetworkConnectTo",
  "",
  {0, 0, 0, 0},
  0
};

void loadConfig() {
  if (EEPROM.read(CONFIG_START + 0) == CONFIG_VERSION[0] &&
      EEPROM.read(CONFIG_START + 1) == CONFIG_VERSION[1] &&
      EEPROM.read(CONFIG_START + 2) == CONFIG_VERSION[2])
    for (unsigned int t=0; t<sizeof(WiFiConfig); t++)
      *((char*)&WiFiConfig + t) = EEPROM.read(CONFIG_START + t);
}

void saveConfig() {
  for (unsigned int t=0; t<sizeof(WiFiConfig); t++)
    EEPROM.write(CONFIG_START + t, *((char*)&WiFiConfig + t));
}
