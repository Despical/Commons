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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import me.despical.commons.number.NumberUtils;
import me.despical.commons.util.WeakLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class LocationSerializer {

	/**
	 * Default world spawn location.
	 */
	public static final Location DEFAULT_LOCATION;

	/**
	 * Default serialized location.
	 */
	public static final String SERIALIZED_LOCATION;

	private static final DecimalFormat DECIMAL_FORMAT;
	private static final String DELIMITER = ", ";
	private static final int EXPECTED_PARTS = 6;

	private LocationSerializer() {
	}

	static {
		DECIMAL_FORMAT = new DecimalFormat("0.000");
		DecimalFormatSymbols formatSymbols = DECIMAL_FORMAT.getDecimalFormatSymbols();
		formatSymbols.setDecimalSeparator('.');
		DECIMAL_FORMAT.setDecimalFormatSymbols(formatSymbols);

		DEFAULT_LOCATION = Bukkit.getWorlds().get(0).getSpawnLocation();
		SERIALIZED_LOCATION = toString(DEFAULT_LOCATION);
	}

	/**
	 * Parses a location from string format: "world, x, y, z, yaw, pitch"
	 *
	 * @param input serialized location string
	 * @return Location object or null if parsing fails
	 */
	public static Location fromString(String input) {
		LocationData data = parseLocationData(input);

		if (data == null) return null;

		World world = Bukkit.getWorld(data.worldName);

		if (world == null) return null;

		return new Location(world, data.x, data.y, data.z, data.yaw, data.pitch);
	}

	/**
	 * Converts location to string format: "world, x, y, z, yaw, pitch"
	 *
	 * @param loc location to serialize
	 * @return serialized location string
	 */
	@NotNull
	public static String toString(Location loc) {
		if (loc == null) {
			return "";
		}

		return String.join(", ",
			loc.getWorld().getName(),
			DECIMAL_FORMAT.format(loc.getX()),
			DECIMAL_FORMAT.format(loc.getY()),
			DECIMAL_FORMAT.format(loc.getZ()),
			DECIMAL_FORMAT.format(loc.getYaw()),
			DECIMAL_FORMAT.format(loc.getPitch())
		);
	}

	/**
	 * Checks if serialized location is the same as default location.
	 *
	 * @param serializedLocation serialized location to check
	 * @return true if it matches default location, false otherwise
	 */
	public static boolean isDefaultLocation(String serializedLocation) {
		return serializedLocation != null && serializedLocation.equals(SERIALIZED_LOCATION);
	}

	/**
	 * Checks if location is the same as default location.
	 *
	 * @param location location to check
	 * @return true if it matches default location, false otherwise
	 */
	public static boolean isDefaultLocation(Location location) {
		return location != null && location.equals(DEFAULT_LOCATION);
	}

	/**
	 * Converts serialized location string to WeakLocation object.
	 *
	 * @param input serialized location string
	 * @return WeakLocation object or null if parsing fails
	 */
	public static WeakLocation convertToWeakLocation(String input) {
		LocationData data = parseLocationData(input);
		if (data == null) return null;

		World world = Bukkit.getWorld(data.worldName);
		Location location = new Location(world, data.x, data.y, data.z, data.yaw, data.pitch);

		return new WeakLocation(data.worldName, location);
	}

	/**
	 * Converts WeakLocation to serialized string format.
	 *
	 * @param location WeakLocation to serialize
	 * @return serialized location string
	 */
	public static String convertWeakLocationToString(WeakLocation location) {
		return toString(location.get());
	}

	/**
	 * Private helper method to parse location data from string.
	 *
	 * @param input serialized location string
	 * @return LocationData object or null if parsing fails
	 */
	private static LocationData parseLocationData(String input) {
		if (input == null) {
			return null;
		}

		String[] parts = input.split(DELIMITER);

		if (parts.length != EXPECTED_PARTS) {
			return null;
		}

		try {
			String worldName = parts[0];
			double x = NumberUtils.getDouble(parts[1]);
			double y = NumberUtils.getDouble(parts[2]);
			double z = NumberUtils.getDouble(parts[3]);
			float yaw = NumberUtils.getFloat(parts[4]);
			float pitch = NumberUtils.getFloat(parts[5]);

			return new LocationData(worldName, x, y, z, yaw, pitch);
		} catch (NumberFormatException exception) {
			exception.printStackTrace();
			return null;
		}
	}

	/**
	 * Private data class to hold parsed location components.
	 */
	private static class LocationData {

		final String worldName;
		final double x, y, z;
		final float yaw, pitch;

		LocationData(String worldName, double x, double y, double z, float yaw, float pitch) {
			this.worldName = worldName;
			this.x = x;
			this.y = y;
			this.z = z;
			this.yaw = yaw;
			this.pitch = pitch;
		}
	}
}