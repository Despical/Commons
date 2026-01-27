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

package dev.despical.commons.scoreboard;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

/**
 * @author Despical
 * <p>
 * Created at 17.06.2020
 */
public abstract class Scoreboard implements AutoUpdatable {

    protected static final String TEAM_PREFIX = "Board_";

    protected final Player holder;
    protected final org.bukkit.scoreboard.Scoreboard scoreboard;
    protected final org.bukkit.scoreboard.Scoreboard previousBoard;
    protected final Objective objective;

    protected boolean activated;
    protected boolean autoUpdateEnabled = true;
    protected ScoreboardHandler handler;
    protected BukkitRunnable updateTask;
    protected long updateInterval = 10L;

    public Scoreboard(Player holder) {
        this.holder = holder;
        this.previousBoard = holder.getScoreboard();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("board", "dummy", Component.empty());
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public abstract void update();

    public void activate() {
        if (activated) return;
        if (handler == null) throw new IllegalStateException("Scoreboard handler not set yet!");

        activated = true;
        holder.setScoreboard(scoreboard);

        if (!autoUpdateEnabled) return;

        updateTask = new BukkitRunnable() {

            @Override
            public void run() {
                if (!holder.isOnline()) {
                    deactivate();
                    return;
                }
                update();
            }
        };

        updateTask.runTaskTimer(ScoreboardLib.getInstance(), 0L, updateInterval);
    }

    public void deactivate() {
        if (!activated) return;
        activated = false;

        if (holder.isOnline()) {
            holder.setScoreboard(previousBoard);
        }

        for (Team team : scoreboard.getTeams()) {
            team.unregister();
        }

        if (updateTask != null) {
            updateTask.cancel();
            updateTask = null;
        }
    }

    @Override
    public long getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public void setUpdateInterval(long updateInterval) {
        if (activated) throw new IllegalStateException("Cannot change interval after activation");
        this.updateInterval = updateInterval;
    }

    @Override
    public void disableAutoUpdate() {
        if (activated) throw new IllegalStateException("Cannot disable auto-update after activation");
        this.autoUpdateEnabled = false;
    }

    public Player getHolder() {
        return holder;
    }

    public org.bukkit.scoreboard.Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Objective getObjective() {
        return objective;
    }

    public boolean isActivated() {
        return activated;
    }

    public ScoreboardHandler getHandler() {
        return handler;
    }

    public Scoreboard setHandler(ScoreboardHandler handler) {
        this.handler = handler;
        return this;
    }
}
