package me.despical.commons.exception;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
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

	private final JavaPlugin plugin;
	private final Set<String> blacklistedClasses;

	public ExceptionLogHandler(JavaPlugin plugin) {
		this.plugin = plugin;
		this.blacklistedClasses = new HashSet<>();

		this.plugin.getServer().getLogger().addHandler(this);
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
	public void close() throws SecurityException {}

	@Override
	public void flush() {}

	@Override
	public void publish(LogRecord record) {
		try {
			Throwable throwable = record.getThrown();

			if (throwable == null || throwable.getCause() == null) {
				return;
			}

			StackTraceElement[] element = throwable.getCause().getStackTrace();

			if (element.length == 0 || element[0] == null || !element[0].getClassName().contains(mainPackage)) {
				return;
			}

			if(containsBlacklistedClass(throwable)) {
				return;
			}

			Throwable exception = throwable.getCause() != null ? throwable.getCause() : throwable;
			StringBuilder stacktrace = new StringBuilder(exception.getClass().getSimpleName());

			if (exception.getMessage() != null) {
				stacktrace.append(" (").append(exception.getMessage()).append(')');
			}

			stacktrace.append("\n");

			for (StackTraceElement traceElement :  exception.getStackTrace()) {
				stacktrace.append(traceElement.toString()).append("\n");
			}

			plugin.getLogger().log(Level.WARNING, "[Reporter service] <<-----------------------------[START]----------------------------->>");
			plugin.getLogger().log(Level.WARNING, stacktrace.toString());
			plugin.getLogger().log(Level.WARNING, "[Reporter service] <<------------------------------[END]------------------------------>>");

			record.setThrown(null);
			record.setMessage(recordMessage);
		} catch (ArrayIndexOutOfBoundsException ignored) {}
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