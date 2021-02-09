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

package me.despical.commons.scoreboard.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import me.despical.commons.scoreboard.ScoreboardLib;
import me.despical.commons.scoreboard.common.Strings;
import me.despical.commons.compat.VersionResolver;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * @author Despical
 * <p>
 * Created at 17.06.2020
 */
public class SimpleScoreboard implements Scoreboard {

	private static final String TEAM_PREFIX = "Board_";
	private static int TEAM_COUNTER = 0;

	private final org.bukkit.scoreboard.Scoreboard scoreboard;
	private final Objective objective;

	protected Player holder;
	protected long updateInterval = 10L;

	private boolean activated;
	private ScoreboardHandler handler;
	private Map<FakePlayer, Integer> entryCache = new ConcurrentHashMap<>();
	private Table<String, Integer, FakePlayer> playerCache = HashBasedTable.create();
	private Table<Team, String, String> teamCache = HashBasedTable.create();
	private BukkitRunnable updateTask;

	public SimpleScoreboard(Player holder) {
		this.holder = holder;
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		scoreboard.registerNewObjective("board", "dummy").setDisplaySlot(DisplaySlot.SIDEBAR);
		objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
	}

	@Override
	public void activate() {
		if (activated) return;
		if (handler == null) throw new IllegalArgumentException("Scoreboard handler not set!");
		activated = true;
		holder.setScoreboard(scoreboard);
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
				holder.setScoreboard((Bukkit.getScoreboardManager().getMainScoreboard()));
			}
		}

		for (Team team : teamCache.rowKeySet()) {
			team.unregister();
		}

		updateTask.cancel();
	}

	@Override
	public boolean isActivated() {
		return activated;
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
		return updateInterval;
	}

	@Override
	public SimpleScoreboard setUpdateInterval(long updateInterval) {
		if (activated) throw new IllegalStateException("Scoreboard is already activated!");
		this.updateInterval = updateInterval;
		return this;
	}

	@Override
	public Player getHolder() {
		return holder;
	}

	private void update() {
		if (!holder.isOnline()) {
			deactivate();
			return;
		}

		String handlerTitle = handler.getTitle(holder);
		String finalTitle = Strings.format(handlerTitle != null ? handlerTitle : ChatColor.BOLD.toString());
		if (!objective.getDisplayName().equals(finalTitle)) objective.setDisplayName(Strings.format(finalTitle));

		List<Entry> passed = handler.getEntries(holder);
		Map<String, Integer> appeared = new HashMap<>();
		Map<FakePlayer, Integer> current = new HashMap<>();

		if (passed == null) return;
		for (Entry entry : passed) {
			String key = entry.getName();
			Integer score = entry.getPosition();
			String appearance;

			if (VersionResolver.isCurrentEqualOrHigher(VersionResolver.ServerVersion.v1_14_R1)) {
				if (key.length() > 144) key = key.substring(0, 143);
				if (key.length() > 64) {
					appearance = key.substring(64);
				} else {
					appearance = key;
				}
			} else {
				if (key.length() > 48) key = key.substring(0, 47);
				if (key.length() > 16) {
					appearance = key.substring(16);
				} else {
					appearance = key;
				}
			}

			if (!appeared.containsKey(appearance)) appeared.put(appearance, -1);
			appeared.put(appearance, appeared.get(appearance) + 1);
			FakePlayer faker = getFakePlayer(key, appeared.get(appearance));
			objective.getScore(faker).setScore(score);
			entryCache.put(faker, score);
			current.put(faker, score);
		}

		appeared.clear();

		for (FakePlayer fakePlayer : entryCache.keySet()) {
			if (!current.containsKey(fakePlayer)) {
				entryCache.remove(fakePlayer);
				scoreboard.resetScores(fakePlayer.getName());
			}
		}
	}

	private FakePlayer getFakePlayer(String text, int offset) {
		Team team = null;
		String name;

		if (text.length() <= 16) {
			name = text + Strings.repeat(" ", offset);
		} else {
			String prefix;
			String suffix = "";
			offset++;

			if (VersionResolver.isCurrentEqualOrHigher(VersionResolver.ServerVersion.v1_14_R1)) {
				if (text.length() > 63) {
					prefix = text.substring(0, 64 - offset);
					name = ChatColor.getLastColors(prefix) + text.substring(64 - offset);
					if (name.length() > 16) name = name.substring(0, 16);
					if (text.length() > 80) suffix = ChatColor.getLastColors(name) + text.substring(80 - offset - 2);
				} else {
					prefix = text.substring(0, text.length() - offset);
					name = ChatColor.getLastColors(prefix) + text.substring(text.length() - offset);
					if (name.length() > 16) name = name.substring(0, 16);
				}
			} else {
				prefix = text.substring(0, 16 - offset);
				name = ChatColor.getLastColors(prefix) + text.substring(16 - offset);
				if (name.length() > 16) name = name.substring(0, 16);

				if (text.length() > 32) suffix = ChatColor.getLastColors(name) + text.substring(32 - offset - 2);
			}

			for (Team other : teamCache.rowKeySet()) {
				if (other.getPrefix().equals(prefix) && other.getSuffix().equals(suffix)) {
					team = other;
				}
			}

			if (team == null) {
				team = scoreboard.registerNewTeam(TEAM_PREFIX + TEAM_COUNTER++);
				team.setPrefix(prefix);
				team.setSuffix(suffix);
				teamCache.put(team, prefix, suffix);
			}
		}

		FakePlayer faker;

		if (!playerCache.contains(name, offset)) {
			faker = new FakePlayer(name, team, offset);
			playerCache.put(name, offset, faker);

			if (faker.getTeam() != null) {
				faker.getTeam().addPlayer(faker);
			}
		} else {
			faker = playerCache.get(name, offset);

			if (team != null && faker.getTeam() != null) {
				faker.getTeam().removePlayer(faker);
			}

			faker.setTeam(team);

			if (faker.getTeam() != null) {
				faker.getTeam().addPlayer(faker);
			}
		}
		return faker;
	}

	public Objective getObjective() {
		return objective;
	}

	public org.bukkit.scoreboard.Scoreboard getScoreboard() {
		return scoreboard;
	}

	private static class FakePlayer implements OfflinePlayer {

		private final String name;
		private Team team;
		private int offset;

		FakePlayer(String name, Team team, int offset) {
			this.name = name;
			this.team = team;
			this.offset = offset;
		}

		public Team getTeam() {
			return team;
		}

		public void setTeam(Team team) {
			this.team = team;
		}

		public int getOffset() {
			return offset;
		}

		public String getFullName() {
			if (team == null) return name;
			if (team.getSuffix() == null) return team.getPrefix() + name;
			return team.getPrefix() + name + team.getSuffix();
		}

		@Override
		public boolean isOnline() {
			return true;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public UUID getUniqueId() {
			return UUID.randomUUID();
		}

		@Override
		public boolean isBanned() {
			return false;
		}

		@Override
		public boolean isWhitelisted() {
			return false;
		}

		@Override
		public void setWhitelisted(boolean whitelisted) {
		}

		@Override
		public Player getPlayer() {
			return null;
		}

		@Override
		public long getFirstPlayed() {
			return 0;
		}

		@Override
		public long getLastPlayed() {
			return 0;
		}

		@Override
		public boolean hasPlayedBefore() {
			return false;
		}

		@Override
		public Location getBedSpawnLocation() {
			return null;
		}

		@Override
		public long getLastLogin() {
			return 0;
		}

		@Override
		public long getLastSeen() {
			return 0;
		}

		@Override
		public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
		}

		@Override
		public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
		}

		@Override
		public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
		}

		@Override
		public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
		}

		@Override
		public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException {
		}

		@Override
		public int getStatistic(Statistic statistic) throws IllegalArgumentException {
			return 0;
		}

		@Override
		public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
		}

		@Override
		public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
		}

		@Override
		public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
			return 0;
		}

		@Override
		public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
		}

		@Override
		public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
		}

		@Override
		public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException {
		}

		@Override
		public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
		}

		@Override
		public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
		}

		@Override
		public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
			return 0;
		}

		@Override
		public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) throws IllegalArgumentException {
		}

		@Override
		public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
		}

		@Override
		public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
		}

		@Override
		public Map<String, Object> serialize() {
			return null;
		}

		@Override
		public boolean isOp() {
			return false;
		}

		@Override
		public void setOp(boolean op) {
		}

		@Override
		public String toString() {
			return "FakePlayer{" +
				"name='" + name + '\'' +
				", team=" + team
				+ '}';
		}
	}
}