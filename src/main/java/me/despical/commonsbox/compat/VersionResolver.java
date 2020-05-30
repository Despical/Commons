package me.despical.commonsbox.compat;

import org.bukkit.Bukkit;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class VersionResolver {

	private VersionResolver() {
	}

	public static ServerVersion resolveVersion() {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		if (version.equalsIgnoreCase("v1_8_R3")) {
			return ServerVersion.MINECRAFT_1_8_R3;
		} else if (version.equalsIgnoreCase("v1_9_R1")) {
			return ServerVersion.MINECRAFT_1_9_R1;
		} else if (version.equalsIgnoreCase("v1_9_R2")) {
			return ServerVersion.MINECRAFT_1_9_R2;
		} else if (version.equalsIgnoreCase("v1_10_R1")) {
			return ServerVersion.MINECRAFT_1_10_R1;
		} else if (version.equalsIgnoreCase("v1_11_R1")) {
			return ServerVersion.MINECRAFT_1_11_R1;
		} else if (version.equalsIgnoreCase("v1_12_R1")) {
			return ServerVersion.MINECRAFT_1_12_R1;
		} else if (version.equalsIgnoreCase("v1_13_R1")) {
			return ServerVersion.MINECRAFT_1_13_R1;
		} else if (version.equalsIgnoreCase("v1_13_R2")) {
			return ServerVersion.MINECRAFT_1_13_R2;
		}
		return ServerVersion.OTHER;
	}

	public static boolean isBefore1_13() {
		ServerVersion version = resolveVersion();
		return version == ServerVersion.MINECRAFT_1_8_R3 || version == ServerVersion.MINECRAFT_1_9_R1 || version == ServerVersion.MINECRAFT_1_9_R2
		|| version == ServerVersion.MINECRAFT_1_10_R1 || version == ServerVersion.MINECRAFT_1_11_R1 || version == ServerVersion.MINECRAFT_1_12_R1;
	}

	public enum ServerVersion {
		MINECRAFT_1_8_R3, MINECRAFT_1_9_R1, MINECRAFT_1_9_R2, MINECRAFT_1_10_R1, MINECRAFT_1_11_R1, 
		MINECRAFT_1_12_R1, MINECRAFT_1_13_R1, MINECRAFT_1_13_R2, OTHER
	}
}