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

package dev.despical.commons.scoreboard.type;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import dev.despical.commons.scoreboard.Scoreboard;
import dev.despical.commons.scoreboard.common.Entry;
import dev.despical.commons.util.Strings;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Supports up to 48 characters.
 *
 * @author Despical
 * <p>
 * Created at 18.02.2025
 */
public class LegacySimpleScoreboard extends Scoreboard {

	private static int TEAM_COUNTER;

	private final Map<FakePlayer, Integer> entryCache;
	private final Table<Team, String, String> teamCache;
	private final Table<String, Integer, FakePlayer> playerCache;

	public LegacySimpleScoreboard(Player holder) {
        super(holder);
        this.entryCache = new ConcurrentHashMap<>();
        this.teamCache = HashBasedTable.create();
        this.playerCache = HashBasedTable.create();
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

		String title = Strings.format(handler.getTitle(holder));

		if (!objective.getDisplayName().equals(title)) {
			objective.setDisplayName(title);
		}

		List<Entry> passed = handler.getEntries(holder);
		Map<String, Integer> appeared = new HashMap<>(passed.size());
		Set<FakePlayer> current = new HashSet<>(passed.size());

		for (Entry entry : passed) {
			String key = Strings.format(entry.getContext());

			if (key.length() > 48) {
				key = key.substring(0, 48);
			}

			String appearance = key.length() > 16 ? key.substring(16) : key;

			int val = appeared.computeIfAbsent(appearance, k -> -1) + 1;
			appeared.put(appearance, val);

			FakePlayer faker = getFakePlayer(key, val);
			Score fakePlayerScore = objective.getScore(faker);
			int score = entry.getPosition();

			for (String ks : scoreboard.getEntries()) {
				Score sc = objective.getScore(ks);

				if (score == sc.getScore() && !sc.getEntry().equals(fakePlayerScore.getEntry())) {
					scoreboard.resetScores(ks);
					break;
				}
			}

			fakePlayerScore.setScore(score);

			entryCache.put(faker, score);
			current.add(faker);
		}

		for (FakePlayer fakePlayer : entryCache.keySet()) {
			if (!current.contains(fakePlayer)) {
				entryCache.remove(fakePlayer);
				scoreboard.resetScores(fakePlayer.toString());
			}
		}
	}

	private FakePlayer getFakePlayer(String text, int offset) {
		Team team = null;
		String name;
		int length = text.length();

		if (length <= 16) {
			name = text + Strings.repeat(" ", offset);
		} else {
			offset++;

			int index = 16 - offset;

			String prefix = text.substring(0, index);
			name = text.substring(index);

			if (name.length() > 16) {
				name = name.substring(0, 16);
			}

			String suffix = "";
			if (length > 32) {
				suffix = text.substring(32 - offset);
			}

			for (Team other : teamCache.rowKeySet()) {
				if (other.getPrefix().equals(prefix) && suffix.equals(other.getSuffix())) {
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

		FakePlayer fakePlayer = playerCache.get(name, offset);

		if (fakePlayer == null) {
			fakePlayer = new FakePlayer(name, team);
			playerCache.put(name, offset, fakePlayer);
		} else {
			if (team != null && fakePlayer.team != null) {
				fakePlayer.team.removePlayer(fakePlayer);
			}

			fakePlayer.team = team;
		}

		if (fakePlayer.team != null) {
			fakePlayer.team.addPlayer(fakePlayer);
		}

		return fakePlayer;
	}

	private static class FakePlayer implements OfflinePlayer {

		private final UUID randomId = UUID.randomUUID();

		private final String name;
		private Team team;

		FakePlayer(String name, Team team) {
			this.name = name;
			this.team = team;
		}

		@Override
		public boolean isOnline() {
			return true;
		}

		@Override
		public boolean isConnected() {
			return false;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public @NotNull UUID getUniqueId() {
			return randomId;
		}

		@Override
		public @NotNull PlayerProfile getPlayerProfile() {
			return null;
		}

		@Override
		public boolean isBanned() {
			return false;
		}

		@Override
		public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Date date, @Nullable String s1) {
			return null;
		}

		@Override
		public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Instant instant, @Nullable String s1) {
			return null;
		}

		@Override
		public <E extends BanEntry<? super PlayerProfile>> @Nullable E ban(@Nullable String s, @Nullable Duration duration, @Nullable String s1) {
			return null;
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
		public void incrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {

		}

		@Override
		public void decrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {

		}

		@Override
		public void incrementStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {

		}

		@Override
		public void decrementStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {

		}

		@Override
		public void setStatistic(@NotNull Statistic statistic, int i) throws IllegalArgumentException {

		}

		@Override
		public int getStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
			return 0;
		}

		@Override
		public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {

		}

		@Override
		public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {

		}

		@Override
		public int getStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
			return 0;
		}

		@Override
		public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {

		}

		@Override
		public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {

		}

		@Override
		public void setStatistic(@NotNull Statistic statistic, @NotNull Material material, int i) throws IllegalArgumentException {

		}

		@Override
		public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {

		}

		@Override
		public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {

		}

		@Override
		public int getStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
			return 0;
		}

		@Override
		public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) throws IllegalArgumentException {

		}

		@Override
		public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) {

		}

		@Override
		public void setStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int i) {

		}

		@Override
		public @Nullable Location getLastDeathLocation() {
			return null;
		}

		@Override
		public @NotNull Map<String, Object> serialize() {
			return Map.of();
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
