/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2024 Despical
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.despical.commons.serializer;

import java.io.File;
import java.util.*;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class InventorySerializer {

	private static final Set<String> doNotSerialize;

	private InventorySerializer() {}

	static {
		doNotSerialize = new HashSet<>();
	}

	public static void addNonSerializableElements(String... elements) {
		Collections.addAll(doNotSerialize, elements);
	}

	public static boolean saveInventoryToFile(JavaPlugin plugin, Player player) {
		String uuid = player.getUniqueId().toString();
		PlayerInventory inventory = player.getInventory();
		File path = new File(plugin.getDataFolder(), "inventories");

		try {
			File invFile = new File(plugin.getDataFolder() + File.separator + "inventories" + File.separator, uuid + ".inventory");

			if (!path.exists()) {
				path.mkdir();
			}

			if (invFile.exists()) {
				invFile.delete();
			}

			FileConfiguration invConfig = YamlConfiguration.loadConfiguration(invFile);

			invConfig.set("exp", player.getExp());
			invConfig.set("level", player.getLevel());
			invConfig.set("health", player.getHealth());
			invConfig.set("health-scale", player.getHealthScale());
			invConfig.set("max-health", player.getMaxHealth());
			invConfig.set("food-level", player.getFoodLevel());
			invConfig.set("saturation", player.getSaturation());
			invConfig.set("fire-ticks", player.getFireTicks());
			invConfig.set("gamemode", player.getGameMode().name());
			invConfig.set("allow-flight", player.getAllowFlight());
			invConfig.set("inventory-size", inventory.getSize());
			invConfig.set("max-stack-size", inventory.getMaxStackSize());

			List<String> activePotions = new ArrayList<>();

			for (PotionEffect potion : player.getActivePotionEffects()) {
				activePotions.add(potion.getType().getName() + "#" + potion.getDuration() + "#" + potion.getAmplifier());
			}

			invConfig.set("potion-effects", activePotions);

			if (inventory.getHolder() instanceof Player) {
				invConfig.set("inventory-holder", inventory.getHolder().getName());
			}

			ItemStack[] invContents = inventory.getContents();

			for (int i = 0; i < invContents.length; i++) {
				ItemStack itemInInv = invContents[i];

				if (itemInInv != null && itemInInv.getType() != Material.AIR) {
					invConfig.set("slot-" + i, itemInInv);
				}
			}

			ItemStack[] armorContents = inventory.getArmorContents();

			for (int i = 0; i < armorContents.length; i++) {
				ItemStack itemStack = armorContents[i];

				if (itemStack != null && itemStack.getType() != Material.AIR) {
					invConfig.set("armor-" + i, itemStack);
				}
			}

			invConfig.save(invFile);
			return true;
		} catch (Exception ignored) {
			return false;
		}
	}

	private static Inventory getInventoryFromFile(JavaPlugin plugin, String uuid) {
		File file = new File(plugin.getDataFolder() + File.separator + "inventories" + File.separator + uuid + ".inventory");

		if (!file.exists() || file.isDirectory() || !file.getAbsolutePath().endsWith(".inventory")) {
			return plugin.getServer().createInventory(null, 9);
		}

		try {
			FileConfiguration invConfig = YamlConfiguration.loadConfiguration(file);
			Inventory inventory;

			int invSize = invConfig.getInt("inventory-size", 36);

			InventoryHolder invHolder = null;

			if (invConfig.contains("inventory-holder")) {
				invHolder = plugin.getServer().getPlayer(invConfig.getString("inventory-holder"));
			}

			inventory = plugin.getServer().createInventory(invHolder, InventoryType.PLAYER);
			inventory.setMaxStackSize(invConfig.getInt("max-stack-size", 64));

			try {
				ItemStack[] invContents = new ItemStack[invSize];

				for (int i = 0; i < invSize; i++) {
					if (invConfig.contains("slot-" + i)) {
						invContents[i] = invConfig.getItemStack("slot-" + i);
					} else {
						invContents[i] = new ItemStack(Material.AIR);
					}
				}

				inventory.setContents(invContents);
			} catch (Exception e) {
				e.printStackTrace();
			}

			file.delete();
			return inventory;
		} catch (Exception ignore) {
			return plugin.getServer().createInventory(null, 9);
		}
	}

	public static void loadInventory(JavaPlugin plugin, Player player) {
		File file = new File(plugin.getDataFolder() + File.separator + "inventories" + File.separator + player.getUniqueId() + ".inventory");

		if (!file.exists() || file.isDirectory() || !file.getAbsolutePath().endsWith(".inventory")) {
			return;
		}

		try {
			FileConfiguration invConfig = YamlConfiguration.loadConfiguration(file);

			try {
				ItemStack[] armor = new ItemStack[player.getInventory().getArmorContents().length];

				for (int i = 0; i < player.getInventory().getArmorContents().length; i++) {
					if (invConfig.contains("armor-" + i)) {
						armor[i] = invConfig.getItemStack("armor-" + i);
					} else {
						armor[i] = new ItemStack(Material.AIR);
					}
				}

				player.getInventory().setArmorContents(armor);
				if (shouldRestore("max-health")) player.setMaxHealth(invConfig.getDouble("max-health"));
				player.setExp(0);
				player.setLevel(0);
				if (shouldRestore("level")) player.setLevel(invConfig.getInt("level"));
				if (shouldRestore("exp")) player.setExp(Float.parseFloat(invConfig.getString("exp")));
				if (shouldRestore("health")) player.setHealth(invConfig.getDouble("health"));
				if (shouldRestore("health-scale")) player.setHealthScale(invConfig.getDouble("health-scale"));
				if (shouldRestore("hunger")) player.setFoodLevel(invConfig.getInt("food-level"));
				if (shouldRestore("saturation")) player.setSaturation(Float.parseFloat(invConfig.getString("saturation")));
				if (shouldRestore("fire-ticks")) player.setFireTicks(invConfig.getInt("fire-ticks"));
				if (shouldRestore("game-mode")) player.setGameMode(GameMode.valueOf(invConfig.getString("gamemode")));
				if (shouldRestore("allow-flight")) player.setAllowFlight(invConfig.getBoolean("allow-flight"));

				List<String> activePotions = invConfig.getStringList("potion-effects");

				for (String potion : activePotions) {
					String[] splitted = potion.split("#");
					player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(splitted[0]), Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2])));
				}
			} catch (Exception ignored) {}

			Inventory inventory = getInventoryFromFile(plugin, player.getUniqueId().toString());

			for (int i = 0; i < inventory.getContents().length; i++) {
				if (inventory.getItem(i) != null) {
					player.getInventory().setItem(i, inventory.getItem(i));
				}
			}

			player.updateInventory();
		} catch (Exception ignored) {}
	}

	private static boolean shouldRestore(String element) {
		return !doNotSerialize.contains(element);
	}
}