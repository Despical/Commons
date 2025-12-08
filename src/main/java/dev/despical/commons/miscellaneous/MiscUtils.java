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

package dev.despical.commons.miscellaneous;

import dev.despical.commons.util.Strings;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 * @see <a href="https://www.spigotmc.org/threads/free-code-sending-perfectly-centered-chat-message.95872">Spigot Thread</a>
 */
public final class MiscUtils {

	private static final Random random = new Random();

	private MiscUtils() {
	}

	public static void spawnRandomFirework(Location location) {
		Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();

		FireworkEffect.Type type = switch (random.nextInt(4)) {
			case 1 -> FireworkEffect.Type.BALL_LARGE;
			case 2 -> FireworkEffect.Type.BURST;
			case 3 -> FireworkEffect.Type.CREEPER;
			default -> FireworkEffect.Type.BALL;
		};

		FireworkEffect effect = FireworkEffect.builder().flicker(random.nextBoolean()).withColor(Color.fromBGR(random.nextInt(250) + 1))
			.withFade(Color.fromBGR(random.nextInt(250) + 1)).with(type).trail(random.nextBoolean()).build();

		fwm.addEffect(effect);
		fwm.setPower(random.nextInt(2) + 1);
		fw.setFireworkMeta(fwm);
	}

	public static void sendCenteredMessage(CommandSender sender, String message) {
		message = Strings.format(message);

		if (message.startsWith("%no_center%")) {
			sender.sendMessage(message.substring(11));
			return;
		}

		String[] lines = message.split("\n", 40);
		StringBuilder returnMessage = new StringBuilder();

		for (String line : lines) {
			int messagePxSize = 0;
			boolean previousCode = false, isBold = false;

			for (char c : line.toCharArray()) {
				if (c == '§') {
					previousCode = true;
				} else if (previousCode) {
					previousCode = false;
					isBold = c == 'l';
				} else {
					DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
					messagePxSize = isBold ? messagePxSize + dFI.getBoldLength() : messagePxSize + dFI.getLength();
					messagePxSize++;
				}
			}

			int toCompensate = 165 - messagePxSize / 2, spaceLength = DefaultFontInfo.SPACE.getLength() + 1, compensated = 0;
			StringBuilder sb = new StringBuilder();

			while (compensated < toCompensate) {
				sb.append(" ");
				compensated += spaceLength;
			}

			returnMessage.append(sb).append(line).append("\n");
		}

		sender.sendMessage(returnMessage.toString());
	}
}