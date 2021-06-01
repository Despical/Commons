/*
 * Commons - Box of common utilities.
 * Copyright (C) 2021 Despical
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.despical.commons.configuration;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class ConfigUtils {

	/**
	 * Get the config file's configuration
	 *
	 * @param plugin to get config file from
	 * @param filename name of the config file
	 * @return file configuration of given file
	 */
	public static FileConfiguration getConfig(JavaPlugin plugin, String filename) {
		File file = new File(plugin.getDataFolder(), filename + ".yml");

		if (filename.contains(File.separator)) {
			new File(plugin.getDataFolder(), filename.replace(filename.substring(filename.indexOf(File.separator)), "")).mkdirs();
		}

		if (!file.exists()) {
			plugin.saveResource(filename + ".yml", false);
		}

		YamlConfiguration config = new YamlConfiguration();

		try {
			config.load(file);
		} catch (InvalidConfigurationException | IOException ex) {
			ex.printStackTrace();
		}

		return config;
	}

	/**
	 * Save specified config file.
	 *
	 * @param plugin to get config file from
	 * @param config file to save
	 * @param name to get config
	 */
	public static void saveConfig(JavaPlugin plugin, FileConfiguration config, String name) {
		try {
			config.save(new File(plugin.getDataFolder(), name + ".yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}