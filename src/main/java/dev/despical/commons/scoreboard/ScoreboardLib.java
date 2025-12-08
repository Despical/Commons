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

package dev.despical.commons.scoreboard;

import com.cryptomorin.xseries.reflection.XReflection;
import dev.despical.commons.scoreboard.type.LegacySimpleScoreboard;
import dev.despical.commons.scoreboard.type.SimpleScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
		if (XReflection.supports(13)) {
			return new SimpleScoreboard(holder);
		}

		return new LegacySimpleScoreboard(holder);
	}

	public static Scoreboard createLegacyScoreboard(Player holder) {
		return new LegacySimpleScoreboard(holder);
	}
}
