package me.despical.commons.util;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author Despical
 * <p>
 * Created at 28.07.2021
 */
public class LogUtils {

	@NotNull
	private static String name = "[Commons Logging Manager]";

	private static Logger logger;

	// Do not cache to allow object's recreation twice
	public static void enableLogging() {
		logger = LogManager.getLogManager().getLogger(name);
	}

	public static void disableLogging() {
		logger = null;
	}

	public static void setLoggerName(@NotNull String loggerName) {
		name = loggerName;
	}

	private static void ensureLoggingEnabled() {
		if (logger == null) {
			throw new NullPointerException();
		}
	}

	public static void log(Level level, String message, Object... params) {
		ensureLoggingEnabled();

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

	public static void log(String... messages) {
		for (String message : messages) log(message);
	}
}