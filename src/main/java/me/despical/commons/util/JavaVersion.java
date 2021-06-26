package me.despical.commons.util;

/**
 * @author Despical
 * <p>
 * Created at 26.06.2021
 */
public enum JavaVersion {

	JAVA_8,
	JAVA_9,
	JAVA_10,
	JAVA_11,
	JAVA_12,
	JAVA_13,
	JAVA_14,
	JAVA_15,
	JAVA_16,
	JAVA_17;

	private static final JavaVersion currentVersion;

	static {
		String version = System.getProperty("java.specification.version");

		currentVersion = version.equals("1.8") ? JAVA_8 : valueOf(version);
	}

	public static JavaVersion getCurrentVersion() {
		return currentVersion;
	}

	private final double value = me.despical.commons.number.NumberUtils.getDouble(toString());

	public boolean isAtLeast(final JavaVersion javaVersion) {
		return this.value >= javaVersion.value;
	}

	public boolean isAt(final JavaVersion javaVersion) {
		return this.value == javaVersion.value;
	}

	@Override
	public String toString() {
		return this == JAVA_8 ? "1.8" : name().substring(5);
	}
}