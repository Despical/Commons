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

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Despical
 * <p>
 * Created at 28.07.2021
 */
public class LogUtils {

	private static String name;

	private static Logger logger;

	public static void enableLogging(String loggerName) {
		setLoggerName(loggerName);
		enableLogging();
	}

	// Do not cache to allow object's recreation twice
	public static void enableLogging() {
		if (name == null) name = "Commons Logging Manager";

		logger = Logger.getLogger(name); // Create a new logger
	}

	public static void disableLogging() {
		logger = null;
	}

	public static boolean isEnabled() {
		return logger != null;
	}

	public static void setLoggerName(@NotNull String loggerName) {
		name = loggerName;
	}

	public static void log(Level level, String message, Object... params) {
		if (logger == null) {
			return;
		}

		logger.log(level, message, params);
	}

	public static void log(Level level, String message) {
		log(level, message, null);
	}

	public static void log(String message) {
		log(Level.INFO, message);
	}

	public static void log(String message, Object... params) {
		log(Level.INFO, message, params);
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void sendConsoleMessage(String message) {
		Bukkit.getConsoleSender().sendMessage(Strings.format(message));
	}
}