package me.despical.commonsbox.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class ExceptionLogHandler extends Handler {

	private String mainClass;
	private List<ExceptionLogger> exceptionLoggers = new ArrayList<>();
	private List<String> blacklistedClasses = new ArrayList<>();

	public ExceptionLogHandler(String mainClass) {
		this.mainClass = mainClass;
	}

	public void addClassBlacklist(String className) {
		blacklistedClasses.add(className);
	}

	@Override
	public void close() throws SecurityException {
	}

	@Override
	public void flush() {
		System.out.flush();
	}
	
	@Override
	public void publish(LogRecord record) {
		Throwable throwable = record.getThrown();
		if (!(throwable instanceof Exception) || !throwable.getClass().getSimpleName().contains("Exception")) {
			return;
		}
		if (throwable.getStackTrace().length == 0
				|| !throwable.getStackTrace()[0].getClassName().contains(mainClass)) {
			return;
		}
		if (containsBlacklistedClass(throwable)) {
			return;
		}
		for (ExceptionLogger logger : exceptionLoggers) {
			logger.receiveException((Exception) throwable);
		}
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

	public String getMainClass() {
		return mainClass;
	}
	
	public List<ExceptionLogger> getExceptionLoggers() {
		return exceptionLoggers;
	}
	
	public List<String> getBlacklistedClasses() {
		return blacklistedClasses;
	}
}