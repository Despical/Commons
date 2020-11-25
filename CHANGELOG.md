### 1.2.0 Release (25.11.2020)
* Added NumberUtils#isLong method to check if given string is primitive type long.
* Added NumberUtils#isShort method to check if given string is primitive type short.
* Changes on scoreboard system:
  * Fixed colors in Minecraft 1.16 and higher versions
  * Fixed not working in Minecraft 1.16.4
  * Some performance updates.

### 1.1.9 Release (03.11.2020)
* Added PlayerUtils class to show and hide players.
* Added new NMS version (v1_16_R3) to VersionResolver class.
* Fixed player skulls in lower versions of Minecraft 1.12.2.

### 1.1.8 Release (31.10.2020)
* Added FileUtils class.

### 1.1.7 Release (29.10.2020)
* Added durability option to Item Builder class.

### 1.1.6 Release (13.10.2020)
* Added ItemUtils#setPlayerHead method for skull metas with thread-safe.
* Added attribute utils class for controlling player attributes in 1.9 and higher versions.

### 1.1.5 Release (06.10.2020)
* Added license header.
* Added test for NumberUtils#serializeInt.
* Fixed static skull item.
* Fixed colors in item name and lore for ItemBuilder.
* Removed ItemBuilder#colorize method.

### 1.1.4 Release (30.09.2020)
* Reworked on XMaterial class.
  * Added lots of new methods.
  * Added missing item stacks.
* Added MigratorUtils class.
* Added new seriazilation method for Inventory size.

### 1.1.3 Release (30.09.2020)
* Added more methods to compare server versions.
* Added new string matcher method to match Hex color codes.
* Fixed Hex color matcher.
* Fixed skull item.
* Removed Bukkit dependency.

### 1.1.2 Release (20.09.2020)
* Added more JavaDoc comments.
* Added compatibility to new Minecraft servers.
* Added new method to check server version.
* Added new method to check if server supporting every version between 1.8 and 1.16.3.
* Changed ServerVersion enum value names.

### 1.1.2 Pre-Release (31.08.2020)
* Generated JavaDocs.

### 1.1.1 Release (30.07.2020)
* Fixed unbreakable option.

### 1.1.0 Release (04.07.2020)
* Added unbreakable option to ItemBuilder

### 1.0.9 Release (28.06.2020)
* Removed JUnit tests.
* Deleted all unsupported builds.
* Now VersionResolver class supports old versions of Minecraft.
* Also fixed inventory serializing for old versions.
* Now InventorySerializer class has a wide range of Minecraft versions support.
* Fixed the new added options in ItemBuilder.

### 1.0.0 - 1.0.8 Releases (30.05.2020 - 28.06.2020)
* They were be removed due to be broken and unoptimized.