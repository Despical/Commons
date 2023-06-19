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

package me.despical.commons.miscellaneous;

import me.despical.commons.ReflectionUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

/**
 * @author Despical
 * @since 1.1.6
 * <p>
 * Created at 13.10.2020
 */
public class AttributeUtils {

	public static void setAttackCooldown(Player player, double value) {
		if (ReflectionUtils.supports(9)) {
			player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(value);
		}
	}

	public static void resetAttackCooldown(Player player) {
		setAttackCooldown(player, 4);
	}

	public static void healPlayer(Player player) {
		if (ReflectionUtils.supports(9)) {
			player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			return;
		}

		player.setHealth(player.getMaxHealth());
	}
}