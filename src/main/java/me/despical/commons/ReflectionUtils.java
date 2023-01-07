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

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * @author Despical
 */
public final class ReflectionUtils {

	public static final String VERSION = parseVersion();

	public static final int VER = Integer.parseInt(VERSION.substring(1).split("_")[1]);

	public static final String
		CRAFTBUKKIT = "org.bukkit.craftbukkit." + VERSION + '.',
		NMS = v(17, "net.minecraft.").orElse("net.minecraft.server." + VERSION + '.');

	private static final MethodHandle PLAYER_CONNECTION, GET_HANDLE, SEND_PACKET;

	static {
		Class<?> entityPlayer = getNMSClass("server.level", "EntityPlayer"), craftPlayer = getCraftClass("entity.CraftPlayer"), playerConnection = getNMSClass("server.network", "PlayerConnection");

		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodHandle sendPacket = null, getHandle = null, connection = null;

		try {
			connection = lookup.findGetter(entityPlayer, v(17, "b").orElse("playerConnection"), playerConnection);
			getHandle = lookup.findVirtual(craftPlayer, "getHandle", MethodType.methodType(entityPlayer));
			sendPacket = lookup.findVirtual(playerConnection, v(18, "a").orElse("sendPacket"), MethodType.methodType(void.class, getNMSClass("network.protocol", "Packet")));
		} catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException ex) {
			ex.printStackTrace();
		}

		PLAYER_CONNECTION = connection;
		SEND_PACKET = sendPacket;
		GET_HANDLE = getHandle;
	}

	private ReflectionUtils() {
	}

	private static String parseVersion() {
		String found = null;

		for (Package pack : Package.getPackages()) {
			if (pack.getName().startsWith("org.bukkit.craftbukkit.v")) {
				found = pack.getName().split("\\.")[3];
				break;
			}
		}

		if (found == null) throw new IllegalArgumentException("Failed to parse server version. Could not find any package starting with name: 'org.bukkit.craftbukkit.v'");
		return found;
	}

	public static <T> VersionHandler<T> v(int version, T handle) {
		return new VersionHandler<>(version, handle);
	}

	public static <T> CallableVersionHandler<T> v(int version, Callable<T> handle) {
		return new CallableVersionHandler<>(version, handle);
	}

	/**
	 * Checks whether the server version is equal or greater than the given version.
	 *
	 * @param version the version to compare the server version with.
	 *
	 * @return true if the version is equal or newer, otherwise false.
	 */
	public static boolean supports(int version) { return VER >= version; }

	/**
	 * Get a NMS (net.minecraft.server) class which accepts a package for 1.17 compatibility.
	 *
	 * @param newPackage the 1.17 package name.
	 * @param name       the name of the class.
	 *
	 * @return the NMS class or null if not found.
	 */
	@Nullable
	public static Class<?> getNMSClass(@Nonnull String newPackage, @Nonnull String name) {
		if (supports(17)) name = newPackage + '.' + name;
		return getNMSClass(name);
	}

	/**
	 * Get a NMS (net.minecraft.server) class.
	 *
	 * @param name the name of the class.
	 *
	 * @return the NMS class or null if not found.
	 */
	@Nullable
	public static Class<?> getNMSClass(@Nonnull String name) {
		try {
			return Class.forName(NMS + name);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Sends a packet to the player asynchronously if they're online.
	 * Packets are thread-safe.
	 *
	 * @param player  the player to send the packet to.
	 * @param packets the packets to send.
	 *
	 * @return the async thread handling the packet.
	 * @see #sendPacketSync(Player, Object...)
	 */
	@Nonnull
	public static CompletableFuture<Void> sendPacket(@Nonnull Player player, @Nonnull Object... packets) {
		return CompletableFuture.runAsync(() -> sendPacketSync(player, packets))
			.exceptionally(ex -> {
				ex.printStackTrace();
				return null;
			});
	}

	/**
	 * Sends a packet to the player synchronously if they're online.
	 *
	 * @param player  the player to send the packet to.
	 * @param packets the packets to send.
	 *
	 * @see #sendPacket(Player, Object...)
	 */
	public static void sendPacketSync(@Nonnull Player player, @Nonnull Object... packets) {
		try {
			Object handle = GET_HANDLE.invoke(player);
			Object connection = PLAYER_CONNECTION.invoke(handle);

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

	/**
	 * Get a CraftBukkit (org.bukkit.craftbukkit) class.
	 *
	 * @param name the name of the class to load.
	 *
	 * @return the CraftBukkit class or null if not found.
	 */
	@Nullable
	public static Class<?> getCraftClass(@Nonnull String name) {
		try {
			return Class.forName(CRAFTBUKKIT + name);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Class<?> getArrayClass(String clazz, boolean nms) {
		clazz = "[L" + (nms ? NMS : CRAFTBUKKIT) + clazz + ';';
		try {
			return Class.forName(clazz);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Class<?> toArrayClass(Class<?> clazz) {
		try {
			return Class.forName("[L" + clazz.getName() + ';');
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
			if (version == this.version) throw new IllegalArgumentException("Cannot have duplicate version handles for version: " + version);
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
			if (version == this.version) throw new IllegalArgumentException("Cannot have duplicate version handles for version: " + version);
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