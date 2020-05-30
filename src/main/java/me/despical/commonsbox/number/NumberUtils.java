package me.despical.commonsbox.number;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class NumberUtils {

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

public static boolean isBetween(int value, int min, int max) {
	return value >= min && value <= max;
	}
}