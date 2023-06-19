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

package me.despical.commons;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Despical
 */
public final class ReflectionUtils {

	public static final String NMS_VERSION;

	static { // This needs to be right below VERSION because of initialization order.
		// This package loop is used to avoid implementation-dependant strings like Bukkit.getVersion() or Bukkit.getBukkitVersion()
		// which allows easier testing as well.
		String found = null;
		for (Package pack : Package.getPackages()) {
			String name = pack.getName();

			// .v because there are other packages.
			if (name.startsWith("org.bukkit.craftbukkit.v")) {
				found = pack.getName().split("\\.")[3];

				// Just a final guard to make sure it finds this important class.
				// As a protection for forge+bukkit implementation that tend to mix versions.
				// The real CraftPlayer should exist in the package.
				// Note: Doesn't seem to function properly. Will need to separate the version
				// handler for NMS and CraftBukkit for softwares like catmc.
				try {
					Class.forName("org.bukkit.craftbukkit." + found + ".entity.CraftPlayer");
					break;
				} catch (ClassNotFoundException e) {
					found = null;
				}
			}
		}
		if (found == null)
			throw new IllegalArgumentException("Failed to parse server version. Could not find any package starting with name: 'org.bukkit.craftbukkit.v'");
		NMS_VERSION = found;
	}

	public static final int MINOR_NUMBER;
	public static final int PATCH_NUMBER;

	static {
		String[] split = NMS_VERSION.substring(1).split("_");
		if (split.length < 1) {
			throw new IllegalStateException("Version number division error: " + Arrays.toString(split) + ' ' + getVersionInformation());
		}

		String minorVer = split[1];
		try {
			MINOR_NUMBER = Integer.parseInt(minorVer);
			if (MINOR_NUMBER < 0)
				throw new IllegalStateException("Negative minor number? " + minorVer + ' ' + getVersionInformation());
		} catch (Throwable ex) {
			throw new RuntimeException("Failed to parse minor number: " + minorVer + ' ' + getVersionInformation(), ex);
		}

		// Bukkit.getBukkitVersion() = "1.12.2-R0.1-SNAPSHOT"
		Matcher bukkitVer = Pattern.compile("^\\d+\\.\\d+\\.(\\d+)").matcher(Bukkit.getBukkitVersion());
		if (bukkitVer.find()) { // matches() won't work, we just want to match the start using "^"
			try {
				// group(0) gives the whole matched string, we just want the captured group.
				PATCH_NUMBER = Integer.parseInt(bukkitVer.group(1));
			} catch (Throwable ex) {
				throw new RuntimeException("Failed to parse minor number: " + bukkitVer + ' ' + getVersionInformation(), ex);
			}
		} else {
			// 1.8-R0.1-SNAPSHOT
			PATCH_NUMBER = 0;
		}
	}

	public static String getVersionInformation() {
		return "(NMS: " + NMS_VERSION + " | " +
			"Minecraft: " + Bukkit.getVersion() + " | " +
			"Bukkit: " + Bukkit.getBukkitVersion() + ')';
	}

	public static Integer getLatestPatchNumberOf(int minorVersion) {
		if (minorVersion <= 0) throw new IllegalArgumentException("Minor version must be positive: " + minorVersion);

		// https://minecraft.fandom.com/wiki/Java_Edition_version_history
		// There are many ways to do this, but this is more visually appealing.
		int[] patches = {
			/* 1 */ 1,
			/* 2 */ 5,
			/* 3 */ 2,
			/* 4 */ 7,
			/* 5 */ 2,
			/* 6 */ 4,
			/* 7 */ 10,
			/* 8 */ 8, // I don't think they released a server version for 1.8.9
			/* 9 */ 4,

			/* 10 */ 2,//          ,_  _  _,
			/* 11 */ 2,//            \o-o/
			/* 12 */ 2,//           ,(.-.),
			/* 13 */ 2,//         _/ |) (| \_
			/* 14 */ 4,//           /\=-=/\
			/* 15 */ 2,//          ,| \=/ |,
			/* 16 */ 5,//        _/ \  |  / \_
			/* 17 */ 1,//            \_!_/
			/* 18 */ 2,
			/* 19 */ 4,
			/* 20 */ 0,
		};

		if (minorVersion > patches.length) return null;
		return patches[minorVersion - 1];
	}

	public static final String
		CRAFTBUKKIT_PACKAGE = "org.bukkit.craftbukkit." + NMS_VERSION + '.',
		NMS_PACKAGE = v(17, "net.minecraft.").orElse("net.minecraft.server." + NMS_VERSION + '.');

	private static final MethodHandle PLAYER_CONNECTION;
	private static final MethodHandle GET_HANDLE;
	private static final MethodHandle SEND_PACKET;

	static {
		Class<?> entityPlayer = getNMSClass("server.level", "EntityPlayer");
		Class<?> craftPlayer = getCraftClass("entity.CraftPlayer");
		Class<?> playerConnection = getNMSClass("server.network", "PlayerConnection");

		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodHandle sendPacket = null, getHandle = null, connection = null;

		try {
			connection = lookup.findGetter(entityPlayer,
				v(20, "c").v(17, "b").orElse("playerConnection"), playerConnection);
			getHandle = lookup.findVirtual(craftPlayer, "getHandle", MethodType.methodType(entityPlayer));
			sendPacket = lookup.findVirtual(playerConnection,
				v(18, "a").orElse("sendPacket"),
				MethodType.methodType(void.class, getNMSClass("network.protocol", "Packet")));
		} catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException ex) {
			ex.printStackTrace();
		}

		PLAYER_CONNECTION = connection;
		SEND_PACKET = sendPacket;
		GET_HANDLE = getHandle;
	}

	private ReflectionUtils() {
	}

	public static <T> VersionHandler<T> v(int version, T handle) {
		return new VersionHandler<>(version, handle);
	}

	public static <T> CallableVersionHandler<T> v(int version, Callable<T> handle) {
		return new CallableVersionHandler<>(version, handle);
	}

	public static boolean supports(int minorNumber) {
		return MINOR_NUMBER >= minorNumber;
	}

	public static boolean supportsPatch(int patchNumber) {
		return PATCH_NUMBER >= patchNumber;
	}

	@Nullable
	public static Class<?> getNMSClass(@Nonnull String newPackage, @Nonnull String name) {
		if (supports(17)) name = newPackage + '.' + name;
		return getNMSClass(name);
	}

	@Nullable
	public static Class<?> getNMSClass(@Nonnull String name) {
		try {
			return Class.forName(NMS_PACKAGE + name);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Nonnull
	public static CompletableFuture<Void> sendPacket(@Nonnull Player player, @Nonnull Object... packets) {
		return CompletableFuture.runAsync(() -> sendPacketSync(player, packets))
			.exceptionally(ex -> {
				ex.printStackTrace();
				return null;
			});
	}

	public static void sendPacketSync(@Nonnull Player player, @Nonnull Object... packets) {
		try {
			Object handle = GET_HANDLE.invoke(player);
			Object connection = PLAYER_CONNECTION.invoke(handle);

			// Checking if the connection is not null is enough. There is no need to check if the player is online.
			if (connection != null) {
				for (Object packet : packets) SEND_PACKET.invoke(connection, packet);
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	@Nullable
	public static Object getHandle(@Nonnull Player player) {
		Objects.requireNonNull(player, "Cannot get handle of null player");
		try {
			return GET_HANDLE.invoke(player);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return null;
		}
	}

	@Nullable
	public static Object getConnection(@Nonnull Player player) {
		Objects.requireNonNull(player, "Cannot get connection of null player");
		try {
			Object handle = GET_HANDLE.invoke(player);
			return PLAYER_CONNECTION.invoke(handle);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return null;
		}
	}

	@Nullable
	public static Class<?> getCraftClass(@Nonnull String name) {
		try {
			return Class.forName(CRAFTBUKKIT_PACKAGE + name);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static final class VersionHandler<T> {
		private int version;
		private T handle;

		private VersionHandler(int version, T handle) {
			if (supports(version)) {
				this.version = version;
				this.handle = handle;
			}
		}

		public VersionHandler<T> v(int version, T handle) {
			if (version == this.version)
				throw new IllegalArgumentException("Cannot have duplicate version handles for version: " + version);
			if (version > this.version && supports(version)) {
				this.version = version;
				this.handle = handle;
			}
			return this;
		}

		public T orElse(T handle) {
			return this.version == 0 ? handle : this.handle;
		}
	}

	public static final class CallableVersionHandler<T> {
		private int version;
		private Callable<T> handle;

		private CallableVersionHandler(int version, Callable<T> handle) {
			if (supports(version)) {
				this.version = version;
				this.handle = handle;
			}
		}

		public CallableVersionHandler<T> v(int version, Callable<T> handle) {
			if (version == this.version)
				throw new IllegalArgumentException("Cannot have duplicate version handles for version: " + version);
			if (version > this.version && supports(version)) {
				this.version = version;
				this.handle = handle;
			}
			return this;
		}

		public T orElse(Callable<T> handle) {
			try {
				return (this.version == 0 ? handle : this.handle).call();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}