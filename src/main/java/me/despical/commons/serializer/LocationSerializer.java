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

package me.despical.commons.serializer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import me.despical.commons.number.NumberUtils;
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

	private static final DecimalFormat decimalFormat;

	private LocationSerializer() {
	}

	static {
		decimalFormat = new DecimalFormat("0.000");
		DecimalFormatSymbols formatSymbols = decimalFormat.getDecimalFormatSymbols();
		formatSymbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(formatSymbols);

		DEFAULT_LOCATION = Bukkit.getWorlds().get(0).getSpawnLocation();
		SERIALIZED_LOCATION = toString(DEFAULT_LOCATION);
	}

	public static Location fromString(String input) {
		if (input == null) {
			return null;
		}

		String[] parts = input.split(", ");

		if (parts.length != 6) {
			return null;
		}

		try {
			World world = Bukkit.getWorld(parts[0]);

			if (world == null) {
				return null;
			}

			double x = NumberUtils.getDouble(parts[1]);
			double y = NumberUtils.getDouble(parts[2]);
			double z = NumberUtils.getDouble(parts[3]);
			float yaw = NumberUtils.getFloat(parts[4]);
			float pitch = NumberUtils.getFloat(parts[5]);

			return new Location(world, x, y, z, yaw, pitch);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}

		return null;
	}

	@NotNull
	public static String toString(Location loc) {
		if (loc == null) {
			return "";
		}

		return loc.getWorld().getName() + ", " + decimalFormat.format(loc.getX()) + ", " + decimalFormat.format(loc.getY()) + ", " + decimalFormat.format(loc.getZ()) + ", " + decimalFormat.format(loc.getYaw()) + ", " + decimalFormat.format(loc.getPitch());
	}

	/**
	 * Checks if serialized location is the same with default
	 * serialized location.
	 *
	 * @param serializedLocation to check if default location
	 * @return true if it is same with default location otherwise false
	 */
	public static boolean isDefaultLocation(String serializedLocation) {
		return serializedLocation != null && serializedLocation.equals(SERIALIZED_LOCATION);
	}

	/**
	 * Checks if serialized location is the same with default
	 * serialized location.
	 *
	 * @param location to check if default location
	 * @return true if it is same with default location otherwise false
	 */
	public static boolean isDefaultLocation(Location location) {
		return location != null && location.equals(DEFAULT_LOCATION);
	}
}