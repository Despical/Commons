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

package me.despical.commons.miscellaneous;

import me.despical.commons.compat.VersionResolver;
import org.bukkit.block.Block;

/**
 * @author Despical
 * <p>
 * Created at 22.05.2021
 */
public class BlockUtils {

	private BlockUtils() {
	}

	public static void setData(Block block, byte data) {
		if (VersionResolver.isCurrentEqualOrHigher(VersionResolver.ServerVersion.v1_13_R1)) {
			return;
		}

		try {
			Block.class.getMethod("setData", byte.class).invoke(block, data);
		} catch (Exception ignored) {
			// Ignore exception
		}
	}
}