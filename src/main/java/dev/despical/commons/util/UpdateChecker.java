/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2025 Despical
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

package dev.despical.commons.util;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import dev.despical.commons.number.NumberUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class to assist in checking for updates for plugins uploaded to
 * <a href="https://spigotmc.org/resources/">SpigotMC</a>. Before any members of this
 * class are accessed, {@link #init(JavaPlugin, int)} must be invoked by the plugin.
 * <p>
 * This class performs asynchronous queries to <a href="https://spiget.org">SpiGet</a>,
 * an REST server which is updated periodically. If the results of {@link #requestUpdateCheck()}
 * are inconsistent with what is published on SpigotMC, it may be due to SpiGet's cache.
 * Results will be updated in due time.
 *
 * @author Despical and Parker Hawke
 */
public final class UpdateChecker {

	private static final String USER_AGENT = "CHOCO-update-checker", UPDATE_URL = "https://api.spiget.org/v2/resources/%d/versions?size=1&sort=-releaseDate";
	private static final Pattern DECIMAL_SCHEME_PATTERN = Pattern.compile("\\d+(?:\\.\\d+)*");
	public static final VersionScheme VERSION_SCHEME_DECIMAL = (first, second) -> {
		String[] firstSplit = splitVersionInfo(first), secondSplit = splitVersionInfo(second);
		if (firstSplit == null || secondSplit == null) {
			return null;
		}

		for (int i = 0; i < Math.min(firstSplit.length, secondSplit.length); i++) {
			int currentValue = NumberUtils.getInt(firstSplit[i]), newestValue = NumberUtils.getInt(secondSplit[i]);

			if (newestValue > currentValue) {
				return second;
			} else if (newestValue < currentValue) {
				return first;
			}
		}

		return secondSplit.length > firstSplit.length ? second : first;
	};

	private static UpdateChecker instance;
	private final JavaPlugin plugin;
	private final int pluginID;
	private final VersionScheme versionScheme;
	private UpdateResult lastResult;

	private UpdateChecker(JavaPlugin plugin, int pluginID, VersionScheme versionScheme) {
		this.plugin = plugin;
		this.pluginID = pluginID;
		this.versionScheme = versionScheme;
	}

	private static String[] splitVersionInfo(String version) {
		Matcher matcher = DECIMAL_SCHEME_PATTERN.matcher(version);

		return !matcher.find() ? null : matcher.group().split("\\.");
	}

	public static UpdateChecker init(JavaPlugin plugin, int pluginID, VersionScheme versionScheme) {
		Preconditions.checkArgument(plugin != null, "Plugin cannot be null");
		Preconditions.checkArgument(pluginID > 0, "Plugin ID must be greater than 0");
		Preconditions.checkArgument(versionScheme != null, "null version schemes are unsupported");

		return instance == null ? instance = new UpdateChecker(plugin, pluginID, versionScheme) : instance;
	}

	public static UpdateChecker init(JavaPlugin plugin, int pluginID) {
		return init(plugin, pluginID, VERSION_SCHEME_DECIMAL);
	}

	public static UpdateChecker get() {
		Preconditions.checkState(instance != null, "Instance has not yet been initialized. Be sure #init() has been invoked");
		return instance;
	}

	public static boolean isInitialized() {
		return instance != null;
	}

	public void onNewUpdate(Consumer<UpdateResult> resultConsumer) {
		requestUpdateCheck().whenComplete((result, throwable) -> {
			if (result.requiresUpdate()) {
				resultConsumer.accept(result);
			}
		});
	}

	public CompletableFuture<UpdateResult> requestUpdateCheck() {
		return CompletableFuture.supplyAsync(() -> {
			int responseCode;

			try {
				URL url = new URL(String.format(UPDATE_URL, pluginID));
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.addRequestProperty("User-Agent", USER_AGENT);

				InputStreamReader reader = new InputStreamReader(connection.getInputStream());
				responseCode = connection.getResponseCode();

				JsonElement element = new JsonParser().parse(reader);

				if (!element.isJsonArray()) {
					return new UpdateResult(UpdateReason.INVALID_JSON);
				}

				reader.close();

				JsonObject versionObject = element.getAsJsonArray().get(0).getAsJsonObject();
				String current = plugin.getDescription().getVersion(), newest = versionObject.get("name").getAsString();
				String latest = versionScheme.compareVersions(current, newest);

				if (latest == null) {
					return new UpdateResult(UpdateReason.UNSUPPORTED_VERSION_SCHEME);
				} else if (latest.equals(current)) {
					return new UpdateResult(current.equals(newest) ? UpdateReason.UP_TO_DATE : UpdateReason.UNRELEASED_VERSION);
				} else if (latest.equals(newest)) {
					return new UpdateResult(latest);
				}
			} catch (IOException e) {
				return new UpdateResult(UpdateReason.COULD_NOT_CONNECT);
			} catch (JsonSyntaxException e) {
				return new UpdateResult(UpdateReason.INVALID_JSON);
			}

			return new UpdateResult(responseCode == 401 ? UpdateReason.UNAUTHORIZED_QUERY : UpdateReason.UNKNOWN_ERROR);
		});
	}

	public UpdateResult getLastResult() {
		return lastResult;
	}

	public enum UpdateReason {
		NEW_UPDATE, COULD_NOT_CONNECT, INVALID_JSON, UNAUTHORIZED_QUERY, UNRELEASED_VERSION, UNKNOWN_ERROR, UNSUPPORTED_VERSION_SCHEME, UP_TO_DATE
	}

	@FunctionalInterface
	public interface VersionScheme {

		String compareVersions(String first, String second);

	}

	public final class UpdateResult {

		private final UpdateReason reason;
		private final String newestVersion;

		{ // An actual use for initializer blocks. This is madness!
			UpdateChecker.this.lastResult = this;
		}

		private UpdateResult(String newestVersion) {
			this.reason = UpdateReason.NEW_UPDATE;
			this.newestVersion = newestVersion;
		}

		private UpdateResult(UpdateReason reason) {
			Preconditions.checkArgument(reason != UpdateReason.NEW_UPDATE, "Reasons that require updates must also provide the latest version String");
			this.reason = reason;
			this.newestVersion = plugin.getDescription().getVersion();
		}

		public UpdateReason getReason() {
			return reason;
		}

		public boolean requiresUpdate() {
			return reason == UpdateReason.NEW_UPDATE;
		}

		public String getNewestVersion() {
			return newestVersion;
		}
	}
}