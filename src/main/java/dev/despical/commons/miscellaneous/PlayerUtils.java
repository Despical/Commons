/*
 * Commons - Box of the common utilities
 * Copyright (C) 2026  Berke Akçen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.despical.commons.miscellaneous;

import dev.despical.commons.util.ReflectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Despical
 * @since 1.1.9
 * <p>
 * Created at 03.11.2020
 */
public class PlayerUtils {

    private static final boolean IS_MODERN_API = ReflectionUtils.isMethodExists(Player.class, "hidePlayer", JavaPlugin.class, Player.class);

	private PlayerUtils() {
	}

	/**
	 * Hides given player to given other player to avoid using deprecated
	 * methods.
	 *
	 * @param to     player to hide
	 * @param p      player to be hidden
	 * @param plugin instance for method to avoid deprecation
	 */
	public static void hidePlayer(Player to, Player p, JavaPlugin plugin) {
		if (to == null || p == null) {
			return;
		}

		if (IS_MODERN_API) {
			to.hidePlayer(plugin, p);
            return;
		}

    	to.hidePlayer(p);
	}

	/**
	 * Shows given player to given other player to avoid using deprecated
	 * methods.
	 *
	 * @param to     player to show
	 * @param p      player to be shown
	 * @param plugin instance for method to avoid deprecation
	 */
	public static void showPlayer(Player to, Player p, JavaPlugin plugin) {
		if (to == null || p == null) {
			return;
		}

        if (IS_MODERN_API) {
            to.showPlayer(plugin, p);
            return;
        }

    	to.showPlayer(p);
	}
}
