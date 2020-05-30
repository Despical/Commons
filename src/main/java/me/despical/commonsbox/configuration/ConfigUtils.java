package me.despical.commonsbox.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

	public static FileConfiguration getConfig(JavaPlugin plugin, String filename) {
		File file = new File(plugin.getDataFolder() + File.separator + filename + ".yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
				copyResource(plugin.getResource(filename + ".yml"), file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		file = new File(plugin.getDataFolder(), filename + ".yml");
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.load(file);
		} catch(InvalidConfigurationException | IOException ex) {
			ex.printStackTrace();
		}
		return config;
	}
	
	public static void saveConfig(JavaPlugin plugin, FileConfiguration config, String name) {
		try {
			config.save(new File(plugin.getDataFolder(), name + ".yml"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private static void copyResource(InputStream resource, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			int lenght;
			byte[] buf = new byte[1024];
			while((lenght = resource.read(buf)) > 0){
				out.write(buf, 0, lenght);
			}
			out.close();
			resource.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}