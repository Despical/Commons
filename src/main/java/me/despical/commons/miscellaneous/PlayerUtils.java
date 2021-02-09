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

import me.despical.commons.compat.VersionResolver;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Despical
 * @since 1.1.9
 * <p>
 * Created at 03.11.2020
 */
public class PlayerUtils {

	private PlayerUtils() {
	}

	public static void hidePlayer(Player to, Player p, JavaPlugin plugin) {
		if (VersionResolver.isCurrentEqualOrHigher(VersionResolver.ServerVersion.v1_13_R1)) {
			to.hidePlayer(plugin, p);
		} else {
			to.hidePlayer(p);
		}
	}

	public static void showPlayer(Player to, Player p, JavaPlugin plugin) {
		if (VersionResolver.isCurrentEqualOrHigher(VersionResolver.ServerVersion.v1_13_R1)) {
			to.showPlayer(plugin, p);
		} else {
			to.showPlayer(p);
		}
	}
}