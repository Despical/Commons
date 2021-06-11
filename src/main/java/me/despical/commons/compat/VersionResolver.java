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

package me.despical.commons.compat;

import me.despical.commons.number.NumberUtils;

import static me.despical.commons.compat.VersionResolver.ServerVersion.*;

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

	/**
	 * Current version of server.
	 */
	public static final ServerVersion CURRENT_VERSION;

	static {
		CURRENT_VERSION = resolveVersion();
	}

	private VersionResolver() {
	}

	/**
	 * Get the server's version.
	 *
	 * @return version of the server in split NMS format enum
	 */
	private static ServerVersion resolveVersion() {
		String version = me.despical.commons.ReflectionUtils.VERSION;

		try {
			return ServerVersion.valueOf(version);
		} catch(IllegalArgumentException exception) {
			return OTHER;
		}
	}

	/**
	 * Check if the current version is before specified NMS version.
	 *
	 * @param version to check current one is older than that
	 * @return server version is before the given NMS version
	 */
	public static boolean isCurrentLower(ServerVersion version) {
		return CURRENT_VERSION.versionAsInt() < version.versionAsInt();
	}

	/**
	 * Check if the current version is before specified NMS version.
	 *
	 * @param version to check current one is older than that
	 * @return server version is before the given NMS version
	 */
	public static boolean isCurrentEqualOrLower(ServerVersion version) {
		return CURRENT_VERSION.versionAsInt() <= version.versionAsInt();
	}

	/**
	 * Checks if server version is supported or not.
	 *
	 * @return server version is supported
	 */
	public static boolean isAllSupported() {
		return CURRENT_VERSION.versionAsInt() != 0;
	}

	/**
	 * Checks if current version equals to given version.
	 *
	 * @param version must be equals to current
	 * @return true if equals otherwise false
	 */
	public static boolean isCurrentEqual(ServerVersion version) {
		return CURRENT_VERSION == version;
	}

	/**
	 * Checks if current version equals or higher than the given version.
	 *
	 * @param version given version
	 * @return true if current version equals or higher than given one
	 */
	public static boolean isCurrentEqualOrHigher(ServerVersion version) {
		return CURRENT_VERSION.versionAsInt() >= version.versionAsInt();
	}

	/**
	 * Checks if current version is higher than the given version.
	 *
	 * @param version given version
	 * @return true if current version is higher than given one
	 */
	public static boolean isCurrentHigher(ServerVersion version) {
		return CURRENT_VERSION.versionAsInt() > version.versionAsInt();
	}

	/**
	 * Checks if current version is between given two versions.
	 *
	 * @param min version to check
	 * @param max version to check.
	 * @return true if current version is between these two versions, false otherwise.
	 */
	public static boolean isCurrentBetween(ServerVersion min, ServerVersion max) {
		return NumberUtils.isBetween(CURRENT_VERSION.versionAsInt(), min.versionAsInt(), max.versionAsInt());
	}

	/**
	 * Enum values of the each Minecraft version in NMS format.
	 */
	public enum ServerVersion {
		v1_8_R1, v1_8_R2, v1_8_R3, v1_9_R1, v1_9_R2, v1_10_R1, v1_11_R1, v1_12_R1,
		v1_13_R1, v1_13_R2, v1_14_R1, v1_15_R1, v1_16_R1, v1_16_R2, v1_16_R3, v1_17_R1,
		OTHER;

		int versionAsInt() {
			return NumberUtils.getInt(name().replaceAll("[v_R]", ""));
		}
	}
}