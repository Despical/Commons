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

package me.despical.commons.scoreboard.type;

import me.despical.commons.scoreboard.ScoreboardLib;
import me.despical.commons.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Despical
 * <p>
 * Created at 17.06.2020
 */
public class SimpleScoreboard implements Scoreboard {

	private static final String TEAM_PREFIX = "Board_";
	private final org.bukkit.scoreboard.Scoreboard scoreboard;
	private final Objective objective;

	protected Player holder;
	private boolean activated;
	private boolean autoUpdateEnabled = true;
	private ScoreboardHandler handler;
	private BukkitRunnable updateTask;
	private long updateInterval = 10L;

	public SimpleScoreboard(Player holder) {
		this.holder = holder;
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		scoreboard.registerNewObjective("board", "dummy").setDisplaySlot(DisplaySlot.SIDEBAR);
		objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);

		for (int i = 1; i <= 15; i++) {
			scoreboard.registerNewTeam(TEAM_PREFIX + i).addEntry(getEntry(i));
		}
	}

	@Override
	public void activate() {
		if (activated) return;
		if (handler == null) throw new IllegalArgumentException("Scoreboard handler not set!");

		activated = true;

		holder.setScoreboard(scoreboard);

		if (!autoUpdateEnabled) {
			return;
		}

		updateTask = new BukkitRunnable() {
			@Override
			public void run() {
				update();
			}
		};

		updateTask.runTaskTimer(ScoreboardLib.getInstance(), 0, updateInterval);
	}

	@Override
	public void deactivate() {
		if (!activated) return;

		activated = false;

		if (holder.isOnline()) {
			synchronized (this) {
				holder.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			}
		}

		for (Team team : scoreboard.getTeams()) {
			team.unregister();
		}

		Optional.ofNullable(this.updateTask).ifPresent(BukkitRunnable::cancel);
	}

	@Override
	public boolean isActivated() {
		return activated;
	}

	@Override
	public void disableAutoUpdate() {
		if (this.activated) {
			throw new IllegalStateException("You can not disable auto updating task during the scoreboard is updating!");
		}

		this.autoUpdateEnabled = false;
	}

	@Override
	public ScoreboardHandler getHandler() {
		return handler;
	}

	@Override
	public Scoreboard setHandler(ScoreboardHandler handler) {
		this.handler = handler;
		return this;
	}

	@Override
	public long getUpdateInterval() {
		return this.updateInterval;
	}

	@Override
	public SimpleScoreboard setUpdateInterval(long updateInterval) {
		if (activated) throw new IllegalStateException("You can not change update interval during scoreboard is updating!");
		this.updateInterval = updateInterval;
		return this;
	}

	@Override
	public Player getHolder() {
		return holder;
	}

	@Override
	public void update() {
		if (!activated) {
			return;
		}

		if (!holder.isOnline()) {
			deactivate();
			return;
		}

		String handlerTitle = handler.getTitle(holder);
		String finalTitle = handlerTitle != null ? Strings.format(handlerTitle) : ChatColor.BOLD.toString();

		if (!objective.getDisplayName().equals(finalTitle)) {
			objective.setDisplayName(finalTitle);
		}

		List<Entry> passed = handler.getEntries(holder);

		if (passed == null) {
			return;
		}

		List<Integer> current = new ArrayList<>(passed.size());

		for (Entry entry : passed) {
			int score = entry.getPosition();
			Team team = scoreboard.getTeam(TEAM_PREFIX + score);
			String temp = getEntry(score);

			if (!scoreboard.getEntries().contains(temp)) {
				objective.getScore(temp).setScore(score);
			}

			String key = Strings.format(entry.getName());
			int length = key.length();

			String prefix = length > 64 ? key.substring(0, 64) : key;
			String suffix = ChatColor.getLastColors(prefix) + (length != 0 ? (prefix.charAt(prefix.length() - 1) == '§' ? "§" : "") : "") + limitKey(length, key);

			team.setPrefix(prefix);
			team.setSuffix(suffix.length() > 64 ? suffix.substring(0, 64) : suffix);

			current.add(score);
		}

		for (int i = 1; i <= 15; i++) {
			if (!current.contains(i)) {
				String entry = getEntry(i);

				if (scoreboard.getEntries().contains(entry)) {
					scoreboard.resetScores(entry);
				}
			}
		}
	}

	public Objective getObjective() {
		return objective;
	}

	private final ChatColor[] values = ChatColor.values();

	private String getEntry(int slot) {
		return values[slot].toString();
	}

	public org.bukkit.scoreboard.Scoreboard getScoreboard() {
		return scoreboard;
	}

	private String limitKey(int length, String str) {
		if (length > 128) {
			return str.substring(0, 128);
		}

		return length > 64 ? str.substring(64) : "";
	}
}