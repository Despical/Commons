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

	public static void sendConsoleMessage(String message) {
		Bukkit.getConsoleSender().sendMessage(Strings.format(message));
	}
}