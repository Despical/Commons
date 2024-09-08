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
	JAVA_17,
	JAVA_18,
	JAVA_19,
	JAVA_20,
	JAVA_21,
	JAVA_22,
	JAVA_23,
	JAVA_24;

	private static final JavaVersion currentVersion;

	static {
		String version = System.getProperty("java.specification.version");

		currentVersion = version.equals("1.8") ? JAVA_8 : valueOf("JAVA_" + version);
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