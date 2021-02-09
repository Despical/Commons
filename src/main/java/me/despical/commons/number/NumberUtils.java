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

package me.despical.commons.number;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class NumberUtils {

	private NumberUtils() {
	}

	/**
	 * Checks if the String contains only unicode digits.
	 * A decimal point is not a unicode digit and returns false.
	 *
	 * @param str the String to check, may be null
	 * @return    true if only contains digits, and is non-null
	 */
	public static boolean isInteger(String str) {
		if (str == null) {
			return false;
		}

		int length = str.length();

		if (length == 0) {
			return false;
		}

		int i = 0;

		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}

			i = 1;
		}

		for (; i < length; i++) {
			char c = str.charAt(i);

			if (c <= '/' || c >= ':') {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks if the String is primitive type double or not.
	 *
	 * @param str the String to check, may be null
	 * @return    true if double, and is non-null
	 */
	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException ignored) {
			return false;
		}
	}

	/**
     * Checks if the String is primitive type long or not.
	 *
	 * @param str the String to check, may be null
	 * @return    true if long, and is non-null
	 */
	public static boolean isLong(String str) {
		try {
			Long.parseLong(str);
			return true;
		} catch (NumberFormatException ignored) {
			return false;
		}
	}

	/**
	 * Checks if the String is primitive type short.
	 *
	 * @param str the String to check, may be null
	 * @return    true if short, and is non-null
	 */
	public static boolean isShort(String str) {
		try {
			Short.parseShort(str);
			return true;
		} catch (NumberFormatException ignored) {
			return false;
		}
	}

	/**
	 * Checks if the given value is between given values.
	 *
	 * @param value the Integer to check is between values
	 * @param min   the minimum value to check given Integer
	 * @param max   the maximum value to check given Integer
	 * @return      true if value is between min and max values
	 */
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
		return i % 9 == 0 ? i : (i + 9 - 1) / 9 * 9;
	}
}