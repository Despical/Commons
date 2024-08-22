package me.despical.commons.scoreboard.type.legacy;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.despical.commons.scoreboard.ScoreboardLib;
import me.despical.commons.scoreboard.type.Entry;
import me.despical.commons.scoreboard.type.Scoreboard;
import me.despical.commons.scoreboard.type.ScoreboardHandler;
import me.despical.commons.util.Strings;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Despical
 * <p>
 * Created at 18.02.2024
 */
public class LegacySimpleScoreboard implements Scoreboard {

	private static final String TEAM_PREFIX = "Board_";
	private static int TEAM_COUNTER = 0;

	private final org.bukkit.scoreboard.Scoreboard scoreboard;
	private final Objective objective;

	protected Player holder;
	private ScoreboardHandler handler;
	private BukkitRunnable updateTask;
	private long updateInterval = 10L;

	private boolean activated;
	private final Map<FakePlayer, Integer> entryCache = new ConcurrentHashMap<>();
	private final Table<String, Integer, FakePlayer> playerCache = HashBasedTable.create();
	private final Table<Team, String, String> teamCache = HashBasedTable.create();

	public LegacySimpleScoreboard(Player holder) {
		this.holder = holder;
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		scoreboard.registerNewObjective("board", "dummy").setDisplaySlot(DisplaySlot.SIDEBAR);
		objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
	}

	@Override
	public void activate() {
		if (activated) return;
		if (handler == null) throw new IllegalArgumentException("Scoreboard handler is not set!");

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
			synchronized(this) {
				holder.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
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
		return this.updateInterval;
	}

	@Override
	public LegacySimpleScoreboard setUpdateInterval(long updateInterval) {
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
		if (!activated) return;

		if (!holder.isOnline()) {
			deactivate();
			return;
		}

		String handlerTitle = handler.getTitle(holder), finalTitle = handlerTitle != null ? Strings.format(handlerTitle) : ChatColor.BOLD.toString();

		if (!objective.getDisplayName().equals(finalTitle)) {
			objective.setDisplayName(finalTitle);
		}

		List<Entry> passed = handler.getEntries(holder);
		
		if (passed == null) return;

		Map<String, Integer> appeared = new HashMap<>(passed.size());
		Set<FakePlayer> current = new HashSet<>(passed.size());

		for (Entry entry : passed) {
			String key = entry.getName();

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

		appeared.clear();

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

	public Objective getObjective() {
		return objective;
	}

	public org.bukkit.scoreboard.Scoreboard getScoreboard() {
		return scoreboard;
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
		public String getName() {
			return name;
		}

		@Override
		public UUID getUniqueId() {
			return randomId;
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
		public void incrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {

		}

		@Override
		public void decrementStatistic(Statistic statistic, int i) throws IllegalArgumentException {

		}

		@Override
		public void setStatistic(Statistic statistic, int i) throws IllegalArgumentException {

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
		public void incrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {

		}

		@Override
		public void decrementStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {

		}

		@Override
		public void setStatistic(Statistic statistic, Material material, int i) throws IllegalArgumentException {

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
		public void incrementStatistic(Statistic statistic, EntityType entityType, int i) throws IllegalArgumentException {

		}

		@Override
		public void decrementStatistic(Statistic statistic, EntityType entityType, int i) {

		}

		@Override
		public void setStatistic(Statistic statistic, EntityType entityType, int i) {

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