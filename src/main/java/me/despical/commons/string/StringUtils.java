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

package me.despical.commons.string;

/**
 * @author Despical
 * <p>
 * Created at 26.02.2022
 */
public class StringUtils {

	public static String capitalize(final String string, final char... delimiters) {
		char[] chars = string.toCharArray();
		boolean capitalizeNext = true; // to make first char upper case

		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];

			if (isDelimiter(c, delimiters)) {
				capitalizeNext = true;
			} else if (capitalizeNext) {
				chars[i] = Character.toTitleCase(c);
				capitalizeNext = false;
			}
		}

		return new String(chars);
	}

	private static boolean isDelimiter(final char c, final char... delimiters) {
		if (delimiters == null) return Character.isWhitespace(c);

		for (char delimiter : delimiters) {
			if (c == delimiter) {
				return true;
			}
		}

		return false;
	}
}