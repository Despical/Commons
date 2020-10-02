package me.despical.commonsbox.migrator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

public class MigratorUtils {

	public MigratorUtils() {}

	public static void removeLineFromFile(File file, String lineToRemove) {
		try {
			List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
			List<String> updatedLines = lines.stream().filter((s) -> !s.contains(lineToRemove)).collect(Collectors.toList());

			FileUtils.writeLines(file, updatedLines, false);
		} catch (IOException var4) {
			var4.printStackTrace();
			Bukkit.getLogger().warning("[CommonsBox] Something went horribly wrong with migration, please contact Despical!");
		}
	}

	public static void insertAfterLine(File file, String search, String text) {
		try {
			int i = 1;
			List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

			for(Iterator var5 = lines.iterator(); var5.hasNext(); ++i) {
				String line = (String)var5.next();
				if (line.contains(search)) {
					lines.add(i, text);
					Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
					break;
				}
			}
		} catch (IOException var7) {
			var7.printStackTrace();
		}
	}

	public static void addNewLines(File file, String newLines) {
		try {
			FileWriter fw = new FileWriter(file.getPath(), true);
			fw.write(newLines);
			fw.close();
		} catch (IOException var3) {
			var3.printStackTrace();
			Bukkit.getLogger().warning("[CommonsBox] Something went horribly wrong with migration, please contact Despical!");
		}
	}
}