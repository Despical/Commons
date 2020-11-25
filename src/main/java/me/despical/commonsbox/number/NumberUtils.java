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

package me.despical.commonsbox.number;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class NumberUtils {

	private NumberUtils() {}

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

	public static boolean isLong(String s) {
		try {
			Long.parseLong(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public static boolean isShort(String s) {
		try {
			Short.parseShort(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public static boolean isBetween(int value, int min, int max) {
		return value >= min && value <= max;
	}

	/**
	 * Serialize int to use it in Inventories size
	 * ex.you have 38 kits and it will serialize it to 45 (9 * 5)
	 * because it is valid inventory size
	 * next ex. you have 55 items and it will serialize it to 63 (9 * 7) not 54 because it's too less
	 *
	 * @param i integer to serialize
	 * @return serialized number
	 */
	public static int serializeInt(Integer i) {
		if (i == 0) return 9;
		return (i % 9) == 0 ? i : (i + 9 - 1) / 9 * 9;
	}
}