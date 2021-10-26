package me.despical.commons.exception;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author Despical
 * <p>
 * Created at 16.05.2021
 */
public class ExceptionLogHandler extends Handler {

	private String mainPackage, recordMessage;

	private final Plugin plugin;
	private final List<String> blacklistedClasses;

	public ExceptionLogHandler(Plugin plugin) {
		this.plugin = plugin;
		this.blacklistedClasses = new ArrayList<>();
	}

	public void addBlacklistedClass(String... classes) {
		me.despical.commons.util.Collections.addAll(blacklistedClasses, classes);
	}

	public void setMainPackage(String mainPackage) {
		this.mainPackage = mainPackage;
	}

	public void setRecordMessage(String recordMessage) {
		this.recordMessage = recordMessage;
	}

	@Override
	public void publish(LogRecord record) {
		Throwable throwable = record.getThrown();

		if (throwable == null || throwable.getCause() == null) {
			return;
		}

		StackTraceElement[] element = throwable.getCause().getStackTrace();

		if (element.length == 0 || element[0] == null || !element[0].getClassName().contains(mainPackage)) {
			return;
		}

		if (containsBlacklistedClass(throwable)) {
			return;
		}

		record.setThrown(null);

		Exception exception = throwable.getCause() != null ? (Exception) throwable.getCause() : (Exception) throwable;
		StringBuilder stacktrace = new StringBuilder(exception.getClass().getSimpleName());

		if (exception.getMessage() != null) {
			stacktrace.append(" (").append(exception.getMessage()).append(")");
		}

		stacktrace.append("\n");

		for (StackTraceElement str : exception.getStackTrace()) {
			stacktrace.append(str.toString()).append("\n");
		}

		plugin.getLogger().log(Level.WARNING, "[Reporter Service] <<-----------------------------[START]----------------------------->>");
		plugin.getLogger().log(Level.WARNING, stacktrace.toString());
		plugin.getLogger().log(Level.WARNING, "[Reporter Service] <<------------------------------[END]------------------------------>>");

		record.setMessage(recordMessage);
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() throws SecurityException {
	}

	private boolean containsBlacklistedClass(Throwable throwable) {
		for (StackTraceElement element : throwable.getStackTrace()) {
			for (String blacklist : blacklistedClasses) {
				if (element.getClassName().contains(blacklist)) {
					return true;
				}
			}
		}

		return false;
	}
}