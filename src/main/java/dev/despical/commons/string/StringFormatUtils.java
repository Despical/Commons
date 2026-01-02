/*
 * Commons - Box of the common utilities
 * Copyright (C) 2026  Berke Akçen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.despical.commons.string;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public final class StringFormatUtils {

	private static final Date today;
	private static String timeFormat;
	private static DateFormat dateFormatter;

	static {
		timeFormat = "%02d:%02d";
		dateFormatter = new SimpleDateFormat("dd/MM/yy");
		today = new Date();
	}

	private StringFormatUtils() {
	}

	public static String formatIntoMMSS(int time) {
		return String.format(timeFormat, time / 60, time % 60);
	}

	public static void setTimeFormat(@NotNull final String timeFormat) {
		StringFormatUtils.timeFormat = timeFormat;
	}

	public static String formatDate(Date date) {
		return dateFormatter.format(date);
	}

	public static String formatToday() {
		return dateFormatter.format(today);
	}

	public static void setDateFormat(String dateFormat) {
		StringFormatUtils.dateFormatter = new SimpleDateFormat(dateFormat);
	}
}
