/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2023 Despical
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

import me.despical.commons.ReflectionUtils;
import me.despical.commons.string.StringMatcher;
import org.bukkit.ChatColor;

/**
 * @author Despical
 * <p>
 * Created at 17.06.2020
 */
public final class Strings {

	private Strings() {
	}

	public static String format(String string) {
		if (string == null || string.isEmpty()) {
			return "";
		}

		if (ReflectionUtils.supports(16) && string.contains("#") ) {
			string = StringMatcher.matchColorRegex(string);
		}

		return ChatColor.translateAlternateColorCodes('&', string);
	}

	/**
	 * Returns a string consisting of a specific number of concatenated copies of an input string. For
	 * example, {@code repeat("hey", 3)} returns the string {@code "heyheyhey"}.
	 *
	 * @param string any non-null string
	 * @param count the number of times to repeat it; a nonnegative integer
	 * @return a string containing {@code string} repeated {@code count} times (the empty string if
	 *     {@code count} is zero)
	 * @throws IllegalArgumentException if {@code count} is negative
	 */
	public static String repeat(String string, int count) {
		if (count <= 1) {
			return count == 0 ? "" : string;
		} else {
			int len = string.length();
			long longSize = (long) len * (long) count;
			int size = (int) longSize;

			if ((long) size != longSize) {
				throw new ArrayIndexOutOfBoundsException("Required array size too large: " + longSize);
			} else {
				char[] array = new char[size];
				string.getChars(0, len, array, 0);
				int n;

				for (n = len; n < size - n; n <<= 1) {
					System.arraycopy(array, 0, array, n, n);
				}

				System.arraycopy(array, 0, array, n, size - n);
				return new String(array);
			}
		}
	}
}