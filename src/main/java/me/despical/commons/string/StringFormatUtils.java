/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2024 Despical
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

import org.jetbrains.annotations.NotNull;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public final class StringFormatUtils {

	private static String format = "%02d:%02d";

	private StringFormatUtils() {
	}

	public static String formatIntoMMSS(int time) {
		return String.format(format, time / 60, time % 60);
	}

	public static void setFormat(@NotNull final String format) {
		StringFormatUtils.format = format;
	}
}