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

package dev.despical.commons.scoreboard.common.animation;

import dev.despical.commons.util.Strings;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Despical
 * <p>
 * Created at 22.05.2021
 */
public class ScrollableString extends FrameAnimatedString {

	private int position;
	private final List<String> list;
	private ChatColor color = ChatColor.RESET;

	public ScrollableString(String message, int width, int spaceBetween) {
		this.list = new ArrayList<>();

		if (message.length() < width) {
			message = message + Strings.repeat(" ", width - message.length());
		}

		width = Math.max(width - 2, 1);

		if (spaceBetween < 0) spaceBetween = 0;

		for (int i = 0; i < message.length() - width; i++) {
			list.add(message.substring(i, i + width));
		}

		StringBuilder space = new StringBuilder();

		for (int i = 0; i < spaceBetween; ++i) {
			list.add(message.substring(message.length() - width + Math.min(i, width)) + space);

			if (space.length() < width)
				space.append(" ");
		}

		for (int i = 0; i < width - spaceBetween; ++i) {
			list.add(message.substring(message.length() - width + spaceBetween + i) + space + message.substring(0, i));
		}

		for (int i = 0; i < spaceBetween; i++) {
			if (i > space.length()) break;

			list.add(space.substring(0, space.length() - i) + message.substring(0, width - Math.min(spaceBetween, width) + i));
		}
	}

	@Override
	public String next() {
		StringBuilder sb = getNext();

		if (sb.charAt(sb.length() - 1) == ChatColor.COLOR_CHAR) {
			sb.setCharAt(sb.length() - 1, ' ');
		}

		if (sb.charAt(0) == ChatColor.COLOR_CHAR) {
			ChatColor c = ChatColor.getByChar(sb.charAt(1));

			if (c != null) {
				color = c;
				sb = getNext();

				if (sb.charAt(0) != ' ') sb.setCharAt(0, ' ');
			}
		}

		return color + sb.toString();
	}

	private StringBuilder getNext() {
		return new StringBuilder(list.get(position++ % list.size()));
	}
}
