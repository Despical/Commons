/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2024 Despical
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

package me.despical.commons.miscellaneous;

import com.cryptomorin.xseries.reflection.XReflection;
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

	/**
	 * Hides given player to given other player to avoid using deprecated
	 * methods.
	 *
	 * @param to player to hide
	 * @param p player to be hidden
	 * @param plugin instance for method to avoid deprecation
	 */
	public static void hidePlayer(Player to, Player p, JavaPlugin plugin) {
		if (to == null || p == null) {
			return;
		}

		if (XReflection.supports(13)) {
			to.hidePlayer(plugin, p);
		} else {
			to.hidePlayer(p);
		}
	}

	/**
	 * Shows given player to given other player to avoid using deprecated
	 * methods.
	 *
	 * @param to player to show
	 * @param p player to be shown
	 * @param plugin instance for method to avoid deprecation
	 */
	public static void showPlayer(Player to, Player p, JavaPlugin plugin) {
		if (to == null || p == null) {
			return;
		}

		if (XReflection.supports(13)) {
			to.showPlayer(plugin, p);
		} else {
			to.showPlayer(p);
		}
	}

	/**
	 * Makes player can collides with other entities to avoid using deprecated
	 * methods.
	 *
	 * @param player to set collidable
	 * @param collidable value to
	 */
	public static void setCollidable(Player player, boolean collidable) {
		if (player == null) {
			return;
		}

		if (!XReflection.supports(9)) {
			player.spigot().setCollidesWithEntities(collidable);
		} else {
			player.setCollidable(collidable);
		}
	}
}