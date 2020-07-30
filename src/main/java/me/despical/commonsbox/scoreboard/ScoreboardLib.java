package me.despical.commonsbox.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.despical.commonsbox.scoreboard.type.Scoreboard;
import me.despical.commonsbox.scoreboard.type.SimpleScoreboard;

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
		return new SimpleScoreboard(holder);
	}
}