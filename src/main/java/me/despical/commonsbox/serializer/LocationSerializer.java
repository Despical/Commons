package me.despical.commonsbox.serializer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import me.despical.commonsbox.exception.InvalidFormatException;
import me.despical.commonsbox.exception.WorldNotFoundException;

public class LocationSerializer {

	private static DecimalFormat decimalFormat;
	
	static {
		decimalFormat = new DecimalFormat("0.000");
		DecimalFormatSymbols formatSymbols = decimalFormat.getDecimalFormatSymbols();
		formatSymbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(formatSymbols);
	}

	public static Location locationFromString(String input) throws WorldNotFoundException, InvalidFormatException {
		if (input == null) {
			throw new InvalidFormatException();
		}
		
		String[] parts = input.split(",");
		
		if (parts.length != 6) {
			throw new InvalidFormatException();
		}
		
		try {
			double x = Double.parseDouble(parts[1].replace(" ", ""));
			double y = Double.parseDouble(parts[2].replace(" ", ""));
			double z = Double.parseDouble(parts[3].replace(" ", ""));
			float yaw = Float.parseFloat(parts[4].replace(" ", ""));
			float pitch = Float.parseFloat(parts[5].replace(" ", ""));
			
			World world = Bukkit.getWorld(parts[0].trim());
			if (world == null) {
				throw new WorldNotFoundException(parts[0].trim());
			}
			
			return new Location(world, x, y, z, yaw, pitch);
			
		} catch (NumberFormatException ex) {
			throw new InvalidFormatException();
		}
	}
	
	public static String locationToString(Location loc) {
		return (loc.getWorld().getName() + ", " + decimalFormat.format(loc.getX()) + ", " + decimalFormat.format(loc.getY()) + ", " + decimalFormat.format(loc.getZ()) + ", " + decimalFormat.format(loc.getYaw()) + ", " + decimalFormat.format(loc.getPitch()));
	}
}