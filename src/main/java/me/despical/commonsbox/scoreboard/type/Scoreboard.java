package me.despical.commonsbox.scoreboard.type;

import org.bukkit.entity.Player;

/**
 * @author Despical
 * <p>
 * Created at 17.06.2020
 */
public interface Scoreboard {

	void activate();

	void deactivate();

	boolean isActivated();

	ScoreboardHandler getHandler();

	Scoreboard setHandler(ScoreboardHandler handler);

	long getUpdateInterval();

	Scoreboard setUpdateInterval(long updateInterval);

	Player getHolder();
}