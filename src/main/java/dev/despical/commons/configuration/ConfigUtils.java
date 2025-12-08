/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2025 Despical
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

package dev.despical.commons.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

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
	 * @param fileName name of the config file
	 * @return file configuration of given file
	 */
	public static FileConfiguration getConfig(JavaPlugin plugin, String fileName) {
		File file = new File(plugin.getDataFolder(), fileName + ".yml");

		if (fileName.contains(File.separator)) {
			new File(plugin.getDataFolder(), fileName.replace(fileName.substring(fileName.indexOf(File.separator)), "")).mkdirs();
		}

		if (!file.exists()) {
			plugin.saveResource(fileName + ".yml", false);
		}

		try {
			return YamlConfiguration.loadConfiguration(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the config file's configuration, gets the file from the resources.
	 *
	 * @param plugin to get config file from
	 * @param fileName name of the config file
	 * @return file configuration of given file
	 */
	public static FileConfiguration getConfigFromResources(JavaPlugin plugin, String fileName) {
		try {
			InputStream inputStream = plugin.getResource(fileName);

			if (inputStream == null) {
				throw new NullPointerException("The given file could not be found in the resources!");
			}

			return YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
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

	/**
	 * Retrieves, modifies, and saves a configuration file for the given plugin.
	 * <p>
	 * This method loads a configuration file using the provided {@code fileName}, applies
	 * modifications through the specified {@link Consumer} of {@link FileConfiguration}, and
	 * then saves the modified configuration file.
	 *
	 * @param plugin The {@link JavaPlugin} instance from which the configuration is loaded and saved.
	 * @param fileName The name of the configuration file to be loaded and saved.
	 * @param configConsumer A {@link Consumer} that performs operations on the {@link FileConfiguration}.
	 *                       This allows you to modify the configuration before it is saved.
	 */
	public static void writeAndSave(JavaPlugin plugin, String fileName, Consumer<FileConfiguration> configConsumer) {
		FileConfiguration config = ConfigUtils.getConfig(plugin, fileName);

		configConsumer.accept(config);

		ConfigUtils.saveConfig(plugin, config, fileName);
	}
}