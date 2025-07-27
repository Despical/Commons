package me.despical.commons.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;

/**
 * @author Despical
 * <p>
 * Created at 27.07.2025
 */
public class WeakLocation {

	private final String worldName;
	private final Location location;

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
