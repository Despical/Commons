/*
 * CommonsBox - Library box of common utilities.
 * Copyright (C) 2020 Despical
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
	public static boolean isCurrentLower(ServerVersion version) {
		List<ServerVersion> versions = Arrays.asList(values());

		List<ServerVersion> splitVers = versions.subList(0, versions.indexOf(version));
		ServerVersion currentVers = resolveVersion();

		return splitVers.contains(currentVers) && !isCurrentEqual(version) && currentVers != OTHER;
	}

	/**
	 * Check if the current version is before specified NMS version.
	 *
	 * @param version to check current one is older than that
	 * @return server version is before the given NMS version
	 */
	public static boolean isCurrentEqualOrLower(ServerVersion version) {
		List<ServerVersion> versions = Arrays.asList(values());

		List<ServerVersion> splitVers = versions.subList(0, versions.indexOf(version));
		ServerVersion currentVers = resolveVersion();

		return (!splitVers.contains(currentVers)) || isCurrentEqual(version) && currentVers != OTHER;
	}

	/**
	 * Checks if server version is supported or not.
	 *
	 * @return server version is supported
	 */
	public static boolean isAllSupported() {
		return Arrays.stream(values()).anyMatch(version -> resolveVersion() == version && version != OTHER);
	}

	/**
	 * Checks if server version is supported without given one or not.
	 *
	 * @return server version is supported without given one
	 */
	public static boolean isAllSupportedExcept(ServerVersion... versions) {
		return Arrays.stream(values()).filter(version -> !Arrays.asList(versions).contains(version)).anyMatch(version -> resolveVersion() == version && version != OTHER);
	}

	/**
	 * Checks if current version equals to given version.
	 *
	 * @param version must be equals to current
	 * @return true if equals otherwise false
	 */
	public static boolean isCurrentEqual(ServerVersion version) {
		return resolveVersion() == version;
	}

	/**
	 * Checks if current version equals or higher than the given version.
	 *
	 * @param version given version
	 * @return true if current version equals or higher than given one
	 */
	public static boolean isCurrentEqualOrHigher(ServerVersion version) {
		List<ServerVersion> versions = Arrays.asList(values());
		List<ServerVersion> splitVers = versions.subList(versions.indexOf(version), versions.size());
		ServerVersion currentVers = resolveVersion();

		return isCurrentEqual(version) || splitVers.contains(currentVers);
	}

	/**
	 * Checks if current version is higher than the given version.
	 *
	 * @param version given version
	 * @return true if current version is higher than given one
	 */
	public static boolean isCurrentHigher(ServerVersion version) {
		List<ServerVersion> versions = Arrays.asList(values());
		List<ServerVersion> splitVers = versions.subList(versions.indexOf(version), versions.size());
		ServerVersion currentVers = resolveVersion();

		return !isCurrentEqual(version) && splitVers.contains(currentVers);
	}

	/**
	 * Enum values of the each Minecraft version in NMS format.
	 */
	public enum ServerVersion {
		v1_8_R1, v1_8_R2, v1_8_R3, v1_9_R1, v1_9_R2, v1_10_R1, v1_11_R1, v1_12_R1,
		v1_13_R1, v1_13_R2, v1_14_R1, v1_15_R1, v1_16_R1, v1_16_R2, OTHER
	}
}