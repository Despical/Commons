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

import com.cryptomorin.xseries.XPotion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Despical
 * <p>
 * Created at 16.02.2025
 */
public class PotionUtils {

	private PotionUtils() {
	}

	public static PotionEffect hideEffect(PotionEffect potion) {
		return createPotionEffect(potion.getType(), potion.getDuration(), potion.getAmplifier());
	}

	public static PotionEffect hideEffect(XPotion potion, int duration, int amplifier) {
		return createPotionEffect(potion.getPotionEffectType(), duration, amplifier);
	}

	private static PotionEffect createPotionEffect(PotionEffectType type, int duration, int amplifier) {
		try {
			return new PotionEffect(type, duration, amplifier, false, false, false);
		} catch (Throwable ignored) {
			return new PotionEffect(type, duration, amplifier, false, false);
		}
	}
}