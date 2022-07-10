/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2022 Despical
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.despical.commons.compat;

import me.despical.commons.ReflectionUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;

/**
 * A reflection API for titles in Minecraft.
 * Fully optimized - Supports 1.8.8 and above.
 * Requires ReflectionUtils.
 * Messages are not colorized by default.
 * <p>
 * Titles are text messages that appear in the
 * middle of the players screen: https://minecraft.gamepedia.com/Commands/title
 * PacketPlayOutTitle: https://wiki.vg/Protocol#Title
 *
 * @author Despical
 * @version 1.2.1
 * @see <a href="https://github.com/CryptoMorin/XSeries">Original Code</a>
 * @see ReflectionUtils
 */
public final class Titles implements Cloneable {

	private static final Object TITLE, SUBTITLE, TIMES, CLEAR;
	private static final MethodHandle PACKET_PLAY_OUT_TITLE;
	private static final MethodHandle CHAT_COMPONENT_TEXT;

	private String title, subtitle;
	private final int fadeIn, stay, fadeOut;

	static {
		MethodHandle packetCtor = null;
		MethodHandle chatComp = null;

		Object times = null;
		Object title = null;
		Object subtitle = null;
		Object clear = null;

		if (!ReflectionUtils.supports(11)) {
			Class<?> chatComponentText = ReflectionUtils.getNMSClass("ChatComponentText");
			Class<?> packet = ReflectionUtils.getNMSClass("PacketPlayOutTitle");
			Class<?> titleTypes = packet.getDeclaredClasses()[0];

			for (Object type : titleTypes.getEnumConstants()) {
				switch (type.toString()) {
					case "TIMES":
						times = type;
						break;
					case "TITLE":
						title = type;
						break;
					case "SUBTITLE":
						subtitle = type;
						break;
					case "CLEAR":
						clear = type;
				}
			}

			MethodHandles.Lookup lookup = MethodHandles.lookup();
			try {
				chatComp = lookup.findConstructor(chatComponentText, MethodType.methodType(void.class, String.class));

				packetCtor = lookup.findConstructor(packet,
					MethodType.methodType(void.class, titleTypes,
						ReflectionUtils.getNMSClass("IChatBaseComponent"), int.class, int.class, int.class));
			} catch (NoSuchMethodException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		TITLE = title;
		SUBTITLE = subtitle;
		TIMES = times;
		CLEAR = clear;

		PACKET_PLAY_OUT_TITLE = packetCtor;
		CHAT_COMPONENT_TEXT = chatComp;
	}

	public Titles(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		this.title = title;
		this.subtitle = subtitle;
		this.fadeIn = fadeIn;
		this.stay = stay;
		this.fadeOut = fadeOut;
	}

	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public Titles clone() {
		return new Titles(title, subtitle, fadeIn, stay, fadeOut);
	}

	public void send(Player player) {
		sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
	}

	/**
	 * Sends a title message with title and subtitle to a player.
	 *
	 * @param player   the player to send the title to.
	 * @param fadeIn   the amount of ticks for title to fade in.
	 * @param stay     the amount of ticks for the title to stay.
	 * @param fadeOut  the amount of ticks for the title to fade out.
	 * @param title    the title message.
	 * @param subtitle the subtitle message.
	 *
	 * @see #clearTitle(Player)
	 */
	public static void sendTitle(Player player,
								 int fadeIn, int stay, int fadeOut,
								 String title, String subtitle) {
		Objects.requireNonNull(player, "Cannot send title to null player");
		if (title == null && subtitle == null) return;
		if (ReflectionUtils.supports(11)) {
			player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
			return;
		}

		try {
			Object timesPacket = PACKET_PLAY_OUT_TITLE.invoke(TIMES, CHAT_COMPONENT_TEXT.invoke(title), fadeIn, stay, fadeOut);
			ReflectionUtils.sendPacket(player, timesPacket);

			if (title != null) {
				Object titlePacket = PACKET_PLAY_OUT_TITLE.invoke(TITLE, CHAT_COMPONENT_TEXT.invoke(title), fadeIn, stay, fadeOut);
				ReflectionUtils.sendPacket(player, titlePacket);
			}
			if (subtitle != null) {
				Object subtitlePacket = PACKET_PLAY_OUT_TITLE.invoke(SUBTITLE, CHAT_COMPONENT_TEXT.invoke(subtitle), fadeIn, stay, fadeOut);
				ReflectionUtils.sendPacket(player, subtitlePacket);
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	/**
	 * Sends a title message with title and subtitle with normal
	 * fade in, stay and fade out time to a player.
	 *
	 * @param player   the player to send the title to.
	 * @param title    the title message.
	 * @param subtitle the subtitle message.
	 *
	 * @see #sendTitle(Player, int, int, int, String, String)
	 */
	public static void sendTitle(Player player, String title, String subtitle) {
		sendTitle(player, 10, 20, 10, title, subtitle);
	}

	public static Titles parseTitle(ConfigurationSection config) {
		return parseTitle(config, null);
	}

	/**
	 * Parses a title from config.
	 * The configuration section must at least
	 * contain {@code title} or {@code subtitle}
	 *
	 * <p>
	 * <b>Example:</b>
	 * <blockquote><pre>
	 *     ConfigurationSection titleSection = plugin.getConfig().getConfigurationSection("restart-title");
	 *     Titles.sendTitle(player, titleSection);
	 * </pre></blockquote>
	 *
	 * @param config the configuration section to parse the title properties from.
	 */
	public static Titles parseTitle(ConfigurationSection config, Function<String, String> transformers) {
		String title = config.getString("title");
		String subtitle = config.getString("subtitle");

		if (transformers != null) {
			title = transformers.apply(title);
			subtitle = transformers.apply(subtitle);
		}

		int fadeIn = config.getInt("fade-in");
		int stay = config.getInt("stay");
		int fadeOut = config.getInt("fade-out");

		if (fadeIn < 1) fadeIn = 10;
		if (stay < 1) stay = 20;
		if (fadeOut < 1) fadeOut = 10;

		return new Titles(title, subtitle, fadeIn, stay, fadeOut);
	}

	public String getTitle() {
		return title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * Parses and sends a title from the config.
	 *
	 * @param player the player to send the title to.
	 * @param config the configuration section to parse the title properties from.
	 */
	public static Titles sendTitle(Player player, ConfigurationSection config) {
		Titles titles = parseTitle(config, null);
		titles.send(player);
		return titles;
	}

	/**
	 * Clears the title and subtitle message from the player's screen.
	 *
	 * @param player the player to clear the title from.
	 */
	public static void clearTitle(Player player) {
		Objects.requireNonNull(player, "Cannot clear title from null player");
		if (ReflectionUtils.supports(11)) {
			player.resetTitle();
			return;
		}

		Object clearPacket;
		try {
			clearPacket = PACKET_PLAY_OUT_TITLE.invoke(CLEAR, null, -1, -1, -1);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return;
		}

		ReflectionUtils.sendPacket(player, clearPacket);
	}

	/**
	 * Supports pre-1.13 tab method.
	 * Changes the tablist header and footer message for a player.
	 * This is not fully completed as it's not used a lot.
	 * <p>
	 * Headers and footers cannot be null because the client will simply
	 * ignore the packet.
	 *
	 * @param header  the header of the tablist.
	 * @param footer  the footer of the tablist.
	 * @param players players to send this change to.
	 */
	public static void sendTabList(String header, String footer, Player... players) {
		Objects.requireNonNull(players, "Cannot send tab title to null players");
		Objects.requireNonNull(header, "Tab title header cannot be null");
		Objects.requireNonNull(footer, "Tab title footer cannot be null");

		if (ReflectionUtils.supports(13)) {
			// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/browse/src/main/java/org/bukkit/entity/Player.java?until=2975358a021fe25d52a8103f7d7aaeceb3abf245&untilPath=src%2Fmain%2Fjava%2Forg%2Fbukkit%2Fentity%2FPlayer.java
			for (Player player : players) player.setPlayerListHeaderFooter(header, footer);
			return;
		}

		try {
			Class<?> IChatBaseComponent = ReflectionUtils.getNMSClass("network.chat", "IChatBaseComponent");
			Class<?> PacketPlayOutPlayerListHeaderFooter = ReflectionUtils.getNMSClass("network.protocol.game", "PacketPlayOutPlayerListHeaderFooter");

			Method chatComponentBuilderMethod = IChatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class);
			Object tabHeader = chatComponentBuilderMethod.invoke(null, "{\"text\":\"" + header + "\"}");
			Object tabFooter = chatComponentBuilderMethod.invoke(null, "{\"text\":\"" + footer + "\"}");

			Object packet = PacketPlayOutPlayerListHeaderFooter.getConstructor().newInstance();
			Field headerField = PacketPlayOutPlayerListHeaderFooter.getDeclaredField("a"); // Changed to "header" in 1.13
			Field footerField = PacketPlayOutPlayerListHeaderFooter.getDeclaredField("b"); // Changed to "footer" in 1.13

			headerField.setAccessible(true);
			headerField.set(packet, tabHeader);

			footerField.setAccessible(true);
			footerField.set(packet, tabFooter);

			for (Player player : players) ReflectionUtils.sendPacket(player, packet);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}