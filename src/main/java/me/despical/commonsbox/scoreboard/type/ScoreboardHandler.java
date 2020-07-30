package me.despical.commonsbox.scoreboard.type;

import java.util.List;

import org.bukkit.entity.Player;

/**
 * @author Despical
 * <p>
 * Created at 17.06.2020
 */
public interface ScoreboardHandler {

	String getTitle(Player player);

	List<Entry> getEntries(Player player);
}