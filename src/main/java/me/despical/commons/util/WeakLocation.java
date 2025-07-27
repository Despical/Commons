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

package me.despical.commons.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Despical
 * <p>
 * Created at 27.07.2025
 */
public class WeakLocation {

	private final String worldName;
	private final Location location;

	public WeakLocation(Location location) {
		this(Objects.requireNonNull(location.getWorld()).getName(), location);
	}

	public WeakLocation(String worldName, Location location) {
		this.worldName = worldName;
		this.location = location;
	}

	public Location get() {
		if (location == null) {
			return null;
		}

		if (!location.isWorldLoaded()) {
			World world = Bukkit.getWorld(worldName);

			if (world != null) {
				location.setWorld(world);
				return location;
			}

			return null;
		}

		return location;
	}

	public boolean isLoaded() {
		return Optional.ofNullable(location)
			.map(Location::isWorldLoaded)
			.orElse(false);
	}

	public Location orElse(Location location) {
		return Optional.ofNullable(this.location).orElse(location);
	}
}
