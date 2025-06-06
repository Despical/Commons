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

package me.despical.commons.scoreboard;

import me.despical.commons.scoreboard.common.Entry;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Despical
 * <p>
 * Created at 17.06.2020
 */
public interface ScoreboardHandler {

	String getTitle(Player player);

	List<Entry> getEntries(Player player);
}
