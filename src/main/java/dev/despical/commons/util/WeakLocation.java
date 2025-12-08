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

package dev.despical.commons.util;

import dev.despical.commons.serializer.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Despical
 * <p>
 * Created at 27.07.2025
 */
public record WeakLocation(String worldName, Location location) {

	public WeakLocation(Location location) {
		this(Objects.requireNonNull(location.getWorld()).getName(), location);
	}

	public Location get() {
		if (location == null) {
			return null;
		}

		if (!isWorldLoaded()) {
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
		if (location == null) {
			return false;
		}

		return isWorldLoaded();
	}

	private boolean isWorldLoaded() {
		World world = location.getWorld();

		if (world == null) {
			return false;
		}

		return world.equals(Bukkit.getWorld(world.getUID()));
	}

	public Location orElse(Location location) {
		return Optional.ofNullable(this.location).orElse(location);
	}

	public void teleportPlayer(@Nullable Player player) {
		if (player == null) return;

		Location targetLocation = get();

		if (targetLocation == null) return;

		player.teleport(targetLocation);
	}

	@NotNull
	@Override
	public String toString() {
		return LocationSerializer.toString(this.get());
	}
}
