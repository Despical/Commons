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

import dev.despical.commons.scoreboard.common.Entry;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Supports up to 1024 characters.
 *
 * @author Despical
 * <p>
 * Created at 17.06.2020
 */
public class SimpleScoreboard extends Scoreboard {

    private final String[] chatColorIds;

    public SimpleScoreboard(Player holder) {
        super(holder);
        this.chatColorIds = new String[16];

        ChatColor[] colors = ChatColor.values();
        for (int i = 0; i < 16; i++) {
            chatColorIds[i] = colors[i].toString();
        }

        for (int slot = 1; slot < 16; slot++) {
            String entryKey = chatColorIds[slot];
            Team team = scoreboard.registerNewTeam(TEAM_PREFIX + slot);
            team.addEntry(entryKey);
        }
    }

    @Override
    public void update() {
        if (!activated || !holder.isOnline()) {
            if (!holder.isOnline()) deactivate();
            return;
        }

        Component title = handler.getTitle(holder);
        if (!objective.displayName().equals(title)) {
            objective.displayName(title);
        }

        List<Entry> passed = handler.getEntries(holder);
        List<Integer> currentSlots = new ArrayList<>();

        for (Entry entry : passed) {
            int score = entry.getPosition();
            if (score < 1 || score > 15) continue;

            Team team = scoreboard.getTeam(TEAM_PREFIX + score);
            String entryKey = chatColorIds[score];

            if (!scoreboard.getEntries().contains(entryKey)) {
                objective.getScore(entryKey).setScore(score);
            }

            team.prefix(entry.getContext());
            team.suffix(Component.empty());

            currentSlots.add(score);
        }

        for (int slot = 1; slot < 16; slot++) {
            if (!currentSlots.contains(slot)) {
                String entryKey = chatColorIds[slot];
                if (scoreboard.getEntries().contains(entryKey)) {
                    scoreboard.resetScores(entryKey);
                }
            }
        }
    }
}
