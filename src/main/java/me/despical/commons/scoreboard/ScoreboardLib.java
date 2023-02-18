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

package me.despical.commons.scoreboard;

import me.despical.commons.compat.VersionResolver;
import me.despical.commons.scoreboard.type.legacy.LegacySimpleScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.despical.commons.scoreboard.type.Scoreboard;
import me.despical.commons.scoreboard.type.SimpleScoreboard;

/**
 * @author Despical
 * <p>
 * Created at 17.06.2020
 */
public class ScoreboardLib {

	private static Plugin instance;

	public static Plugin getInstance() {
		return instance;
	}

	public static void setPluginInstance(Plugin instance) {
		ScoreboardLib.instance = instance;
	}

	public static Scoreboard createScoreboard(Player holder) {
		if (VersionResolver.isCurrentEqualOrHigher(VersionResolver.ServerVersion.v1_13_R1)) {
			return new SimpleScoreboard(holder);
		}

		return new LegacySimpleScoreboard(holder);
	}
}