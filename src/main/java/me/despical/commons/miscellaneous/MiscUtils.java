/*
 * Commons - Box of common utilities.
 * Copyright (C) 2021 Despical
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

package me.despical.commons.miscellaneous;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * @see <a href="https://www.spigotmc.org/threads/free-code-sending-perfectly-centered-chat-message.95872">Spigot Thread</a>
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class MiscUtils {

	private static final Random random = new Random();

	private MiscUtils() {
	}

	public static void spawnRandomFirework(Location location) {
		Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();

		int rt = random.nextInt(4) + 1;
		FireworkEffect.Type type;

		switch (rt) {
			case 1:
				type = FireworkEffect.Type.BALL;
				break;
			case 2:
				type = FireworkEffect.Type.BALL_LARGE;
				break;
			case 3:
				type = FireworkEffect.Type.BURST;
				break;
			case 4:
				type = FireworkEffect.Type.CREEPER;
				break;
			case 5:
				type = FireworkEffect.Type.STAR;
				break;
			default:
				type = FireworkEffect.Type.BALL;
				break;
		}

		int r1i = random.nextInt(250) + 1;
		int r2i = random.nextInt(250) + 1;

		Color c1 = Color.fromBGR(r1i);
		Color c2 = Color.fromBGR(r2i);

		FireworkEffect effect = FireworkEffect.builder().flicker(random.nextBoolean()).withColor(c1).withFade(c2)
			.with(type).trail(random.nextBoolean()).build();
		fwm.addEffect(effect);

		int rp = random.nextInt(2) + 1;
		fwm.setPower(rp);
		fw.setFireworkMeta(fwm);
	}

	public static void sendCenteredMessage(Player player, String message) {
		if (message == null || message.equals("")) {
			player.sendMessage("");
			return;
		}

		message = ChatColor.translateAlternateColorCodes('&', message);

		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;

		for (char c : message.toCharArray()) {
			if (c == '�') {
				previousCode = true;
			} else if (previousCode) {
				previousCode = false;
				isBold = c == 'l' || c == 'L';
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);

				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}

		int CENTER_PX = 154;
		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		StringBuilder sb = new StringBuilder();

		while (compensated < toCompensate) {
			sb.append(" ");
			compensated += spaceLength;
		}

		player.sendMessage(sb.toString() + message);
	}
}