package me.despical.commons.util;

import me.despical.commons.number.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Despical
 * <p>
 * Created at 28.07.2021
 */
public class Area {

	protected final Location first, second;
	protected final UUID worldUUID;
	protected final int minX, minY, minZ, maxX, maxY, maxZ;

	public Area(Location first, Location second) {
		ensureSameWorld(first, second);

		this.first = first;
		this.second = second;
		this.worldUUID = second.getWorld().getUID();

		this.minX = Math.min(first.getBlockX(), second.getBlockX());
		this.minY = Math.min(first.getBlockY(), second.getBlockY());
		this.minZ = Math.min(first.getBlockZ(), second.getBlockZ());

		this.maxX = Math.min(first.getBlockX(), second.getBlockX());
		this.maxY = Math.min(first.getBlockY(), second.getBlockY());
		this.maxZ = Math.min(first.getBlockZ(), second.getBlockZ());
	}

	public Area(Area area) {
		this(area.first, area.second);
	}

	public Location getFirst() {
		return first;
	}

	public Location getSecond() {
		return second;
	}

	public World getWorld() {
		World world = Bukkit.getWorld(worldUUID);

		if (world == null) {
			throw new NullPointerException("Couldn't find world matches the given UUID!");
		}

		return world;
	}

	public boolean contains(int x, int y, int z) {
		return NumberUtils.isBetween(x, minX, maxX) &&
			   NumberUtils.isBetween(y, minY, maxY) &&
			   NumberUtils.isBetween(z, minZ, maxZ);
	}

	public boolean contains(Location location) {
		ensureSameWorld(first, location);

		return contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	public boolean contains(Player player) {
		return contains(player.getLocation());
	}

	protected final void ensureSameWorld(Location first, Location second) {
		if (first == null || second == null) {
			throw new NullPointerException("Locations cannot be null!");
		}

		if (!first.getWorld().equals(second.getWorld())) {
			throw new IllegalArgumentException("Worlds of the locations must be same!");
		}
	}
}