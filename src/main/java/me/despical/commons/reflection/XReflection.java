package me.despical.commons.reflection;

import me.despical.commons.reflection.jvm.classes.DynamicClassHandle;
import me.despical.commons.reflection.jvm.classes.StaticClassHandle;
import me.despical.commons.reflection.minecraft.MinecraftClassHandle;
import me.despical.commons.reflection.minecraft.MinecraftMapping;
import me.despical.commons.reflection.minecraft.MinecraftPackage;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="https://github.com/CryptoMorin/XSeries/tree/master/src/main/java/com/cryptomorin/xseries/reflection">Original Source Code</a>
 *
 * @author CryptoMorin
 * @author Despical
 * <p>
 * Created at 23.05.2024
 */
public final class XReflection {
	/**
	 * We use reflection mainly to avoid writing a new class for version barrier.
	 * The version barrier is for NMS that uses the Minecraft version as the main package name.
	 * <p>
	 * E.g. EntityPlayer in 1.15 is in the class {@code net.minecraft.server.v1_15_R1}
	 * but in 1.14 it's in {@code net.minecraft.server.v1_14_R1}
	 * In order to maintain cross-version compatibility we cannot import these classes.
	 * <p>
	 * Performance is not a concern for these specific statically initialized values.
	 * <p>
	 * <a href="https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-legacy/">Versions Legacy</a>
	 * <p>
	 * This will no longer work because of
	 * <a href="https://forums.papermc.io/threads/paper-velocity-1-20-4.998/#post-2955">Paper no-relocation</a>
	 * strategy.
	 */
	@Nullable
	public static final String NMS_VERSION = findNMSVersionString();

	@Nullable
	public static String findNMSVersionString() {
		// This needs to be right below VERSION because of initialization order.
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

		return found;
	}

	public static final int MAJOR_NUMBER;
	/**
	 * The raw minor version number.
	 * E.g. {@code v1_17_R1} to {@code 17}
	 *
	 * @see #supports(int)
	 */
	public static final int MINOR_NUMBER;
	/**
	 * The raw patch version number. Refers to the <a href="https://en.wikipedia.org/wiki/Software_versioning">major.minor.patch version scheme</a>.
	 * E.g.
	 * <ul>
	 *     <li>{@code v1.20.4} to {@code 4}</li>
	 *     <li>{@code v1.18.2} to {@code 2}</li>
	 *     <li>{@code v1.19.1} to {@code 1}</li>
	 * </ul>
	 * <p>
	 * I'd not recommend developers to support individual patches at all. You should always support the latest patch.
	 * For example, between v1.14.0, v1.14.1, v1.14.2, v1.14.3 and v1.14.4 you should only support v1.14.4
	 * <p>
	 * This can be used to warn server owners when your plugin will break on older patches.
	 *
	 * @see #supportsPatch(int)
	 */
	public static final int PATCH_NUMBER;

	static {
        /* Old way of doing this.
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
         */

		// NMS_VERSION               = v1_20_R3
		// Bukkit.getBukkitVersion() = 1.20.4-R0.1-SNAPSHOT
		// Bukkit.getVersion()       = git-Paper-364 (MC: 1.20.4)
		Matcher bukkitVer = Pattern
			// <patch> is optional for first releases like "1.8-R0.1-SNAPSHOT"
			.compile("^(?<major>\\d+)\\.(?<minor>\\d+)(?:\\.(?<patch>\\d+))?")
			.matcher(Bukkit.getBukkitVersion());
		if (bukkitVer.find()) { // matches() won't work, we just want to match the start using "^"
			try {
				// group(0) gives the whole matched string, we just want the captured group.
				String patch = bukkitVer.group("patch");
				MAJOR_NUMBER = Integer.parseInt(bukkitVer.group("major"));
				MINOR_NUMBER = Integer.parseInt(bukkitVer.group("minor"));
				PATCH_NUMBER = Integer.parseInt((patch == null || patch.isEmpty()) ? "0" : patch);
			} catch (Throwable ex) {
				throw new RuntimeException("Failed to parse minor number: " + bukkitVer + ' ' + getVersionInformation(), ex);
			}
		} else {
			throw new IllegalStateException("Cannot parse server version: \"" + Bukkit.getBukkitVersion() + '"');
		}
	}

	/**
	 * Gets the full version information of the server. Useful for including in errors.
	 */
	public static String getVersionInformation() {
		// Bukkit.getServer().getMinecraftVersion() is for Paper
		return "(NMS: " + NMS_VERSION + " | " +
			"Parsed: " + MAJOR_NUMBER + '.' + MINOR_NUMBER + '.' + PATCH_NUMBER + " | " +
			"Minecraft: " + Bukkit.getVersion() + " | " +
			"Bukkit: " + Bukkit.getBukkitVersion() + ')';
	}

	/**
	 * Gets the latest known patch number of the given minor version.
	 * For example: 1.14 -> 4, 1.17 -> 10
	 * The latest version is expected to get newer patches, so make sure to account for unexpected results.
	 *
	 * @param minorVersion the minor version to get the patch number of.
	 * @return the patch number of the given minor version if recognized, otherwise null.
	 */
	public static Integer getLatestPatchNumberOf(int minorVersion) {
		if (minorVersion <= 0) throw new IllegalArgumentException("Minor version must be positive: " + minorVersion);

		// https://minecraft.wiki/w/Java_Edition_version_history
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
			/* 20 */ 6,
		};

		if (minorVersion > patches.length) return null;
		return patches[minorVersion - 1];
	}

	/**
	 * Mojang remapped their NMS in 1.17: <a href="https://www.spigotmc.org/threads/spigot-bungeecord-1-17.510208/#post-4184317">Spigot Thread</a>
	 */
	public static final String
		CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName(),
		NMS_PACKAGE = v(17, "net.minecraft").orElse("net.minecraft.server." + NMS_VERSION);
	public static final MinecraftMapping SUPPORTED_MAPPING;

	static {
		MinecraftClassHandle entityPlayer = ofMinecraft()
			.inPackage(MinecraftPackage.NMS, "server.level")
			.map(MinecraftMapping.MOJANG, "ServerPlayer")
			.map(MinecraftMapping.SPIGOT, "EntityPlayer");

		if (ofMinecraft()
			.inPackage(MinecraftPackage.NMS, "server.level")
			.map(MinecraftMapping.MOJANG, "ServerPlayer")
			.exists()) {
			SUPPORTED_MAPPING = MinecraftMapping.MOJANG;
		} else if (ofMinecraft()
			.inPackage(MinecraftPackage.NMS, "server.level")
			.map(MinecraftMapping.MOJANG, "EntityPlayer")
			.exists()) {
			SUPPORTED_MAPPING = MinecraftMapping.SPIGOT;
		} else {
			throw new RuntimeException("Unknown Minecraft mapping " + getVersionInformation(), entityPlayer.catchError());
		}
	}

	private XReflection() {}

	/**
	 * Gives the {@code handle} object if the server version is equal or greater than the given version.
	 * This method is purely for readability and should be always used with {@link VersionHandle#orElse(Object)}.
	 *
	 * @see #v(int, int, Object)
	 * @see VersionHandle#orElse(Object)
	 */
	public static <T> VersionHandle<T> v(int version, T handle) {
		return new VersionHandle<>(version, handle);
	}

	/**
	 * Overload for {@link #v(int, T)} that supports patch versions
	 */
	public static <T> VersionHandle<T> v(int version, int patch, T handle) {
		return new VersionHandle<>(version, patch, handle);
	}

	public static <T> VersionHandle<T> v(int version, Callable<T> handle) {
		return new VersionHandle<>(version, handle);
	}

	public static <T> VersionHandle<T> v(int version, int patch, Callable<T> handle) {
		return new VersionHandle<>(version, patch, handle);
	}

	/**
	 * Checks whether the server version is equal or greater than the given version.
	 *
	 * @param minorNumber the version to compare the server version with.
	 * @return true if the version is equal or newer, otherwise false.
	 * @see #MINOR_NUMBER
	 */
	public static boolean supports(int minorNumber) {
		return MINOR_NUMBER >= minorNumber;
	}

	/**
	 * A more friendly version of {@link #supports(int, int)} for people with OCD.
	 */
	public static boolean supports(int majorNumber, int minorNumber, int patchNumber) {
		if (majorNumber != 1) throw new IllegalArgumentException("Invalid major number: " + majorNumber);
		return supports(minorNumber, patchNumber);
	}

	/**
	 * Checks whether the server version is equal or greater than the given version.
	 *
	 * @param minorNumber the minor version to compare the server version with.
	 * @param patchNumber the patch number to compare the server version with.
	 * @return true if the version is equal or newer, otherwise false.
	 * @see #MINOR_NUMBER
	 * @see #PATCH_NUMBER
	 */
	public static boolean supports(int minorNumber, int patchNumber) {
		return MINOR_NUMBER == minorNumber ? PATCH_NUMBER >= patchNumber : supports(minorNumber);
	}

	/**
	 * Checks whether the server version is equal or greater than the given version.
	 *
	 * @param patchNumber the version to compare the server version with.
	 * @return true if the version is equal or newer, otherwise false.
	 * @see #PATCH_NUMBER
	 * @deprecated use {@link #supports(int, int)}
	 */
	@Deprecated
	public static boolean supportsPatch(int patchNumber) {
		return PATCH_NUMBER >= patchNumber;
	}

	/**
	 * Get a NMS (net.minecraft.server) class which accepts a package for 1.17 compatibility.
	 *
	 * @param packageName the 1.17+ package name of this class.
	 * @param name        the name of the class.
	 * @return the NMS class or null if not found.
	 * @throws RuntimeException if the class could not be found.
	 * @deprecated use {@link #ofMinecraft()} instead.
	 * @see #getNMSClass(String)
	 */
	@Nonnull
	@Deprecated
	public static Class<?> getNMSClass(@Nullable String packageName, @Nonnull String name) {
		if (packageName != null && supports(17)) name = packageName + '.' + name;

		try {
			return Class.forName(NMS_PACKAGE + '.' + name);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Get a NMS {@link #NMS_PACKAGE} class.
	 *
	 * @param name the name of the class.
	 * @return the NMS class or null if not found.
	 * @throws RuntimeException if the class could not be found.
	 * @see #getNMSClass(String, String)
	 * @deprecated use {@link #ofMinecraft()}
	 */
	@Nonnull
	@Deprecated
	public static Class<?> getNMSClass(@Nonnull String name) {
		return getNMSClass(null, name);
	}

	/**
	 * Get a CraftBukkit (org.bukkit.craftbukkit) class.
	 *
	 * @param name the name of the class to load.
	 * @return the CraftBukkit class or null if not found.
	 * @throws RuntimeException if the class could not be found.
	 * @deprecated use {@link #ofMinecraft()} instead.
	 */
	@Nonnull
	@Deprecated
	public static Class<?> getCraftClass(@Nonnull String name) {
		try {
			return Class.forName(CRAFTBUKKIT_PACKAGE + '.' + name);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Nonnull
	public static Class<?> toArrayClass(Class<?> clazz) {
		try {
			return Class.forName("[L" + clazz.getName() + ';');
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("Cannot find array class for class: " + clazz, ex);
		}
	}

	public static MinecraftClassHandle ofMinecraft() {
		return new MinecraftClassHandle();
	}

	public static DynamicClassHandle classHandle() {
		return new DynamicClassHandle();
	}

	public static StaticClassHandle of(Class<?> clazz) {
		return new StaticClassHandle(clazz);
	}

	@SafeVarargs
	public static <T, H extends Handle<T>> AggregateHandle<T, H> any(H... handles) {
		return new AggregateHandle<>(Arrays.asList(handles));
	}
}