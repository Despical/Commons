package me.despical.commonsbox.compat;

import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;

import static me.despical.commonsbox.compat.VersionResolver.ServerVersion.*;

/**
 *
 * An utility class for getting server's version in split NSM format.
 *
 * @author Despical
 * @since 1.0.0
 * <p>
 * Created at 30.05.2020
 */
public class VersionResolver {

	private VersionResolver() {}

	/**
	 * Get the server's version.
	 *
	 * @return version of the server in split NMS format enum
	 */
	public static ServerVersion resolveVersion() {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

		if (version.equalsIgnoreCase("v1_8_R1")) {
			return v1_8_R1;
		} else if (version.equalsIgnoreCase("v1_8_R2")) {
			return v1_8_R2;
		} else if (version.equalsIgnoreCase("v1_8_R3")) {
			return v1_8_R3;
		} else if (version.equalsIgnoreCase("v1_9_R1")) {
			return v1_9_R1;
		} else if (version.equalsIgnoreCase("v1_9_R2")) {
			return v1_9_R2;
		} else if (version.equalsIgnoreCase("v1_10_R1")) {
			return v1_10_R1;
		} else if (version.equalsIgnoreCase("v1_11_R1")) {
			return v1_11_R1;
		} else if (version.equalsIgnoreCase("v1_12_R1")) {
			return v1_12_R1;
		} else if (version.equalsIgnoreCase("v1_13_R1")) {
			return v1_13_R1;
		} else if (version.equalsIgnoreCase("v1_13_R2")) {
			return v1_13_R2;
		} else if (version.equalsIgnoreCase("v1_14_R1")) {
			return v1_14_R1;
		} else if (version.equalsIgnoreCase("v1_15_R1")) {
			return v1_15_R1;
		} else if (version.equalsIgnoreCase("v1_16_R1")) {
			return v1_16_R1;
		} else if (version.equalsIgnoreCase("v_16_R2")) {
			return v1_16_R2;
		}

		return OTHER;
	}

	/**
	 * Check if the current version is before specified NMS version.
	 *
	 * @param version to check current one is older than that
	 * @return server version is before the given NMS version
	 */
	public static boolean isBefore(ServerVersion version) {
		List<ServerVersion> versions = Arrays.asList(ServerVersion.values());
		List<ServerVersion> splitVers = versions.subList(0, versions.indexOf(version) - 1);
		ServerVersion currentVers = resolveVersion();

		return !splitVers.contains(resolveVersion()) && version != currentVers;
	}

	/**
	 * Enum values of the each Minecraft version in NMS format.
	 */
	public enum ServerVersion {
		v1_8_R1, v1_8_R2, v1_8_R3, v1_9_R1, v1_9_R2, v1_10_R1, v1_11_R1, v1_12_R1,
		v1_13_R1, v1_13_R2, v1_14_R1, v1_15_R1, v1_16_R1, v1_16_R2, OTHER
	}
}