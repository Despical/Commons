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

import me.despical.commons.scoreboard.Scoreboard;
import me.despical.commons.scoreboard.common.Entry;
import me.despical.commons.string.StringMatcher;
import me.despical.commons.util.Strings;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * Supports up to 144 characters.
 *
 * @author Despical
 * <p>
 * Created at 17.06.2020
 */
public class SimpleScoreboard extends Scoreboard {

    private final ChatColor[] chatColors;

    public SimpleScoreboard(Player holder) {
        super(holder);
        this.chatColors = ChatColor.values();

        for (int slot = 1; slot < 16; slot++) {
            String entry = getEntry(slot);

            Team team = scoreboard.registerNewTeam(TEAM_PREFIX + slot);
            team.addEntry(entry);
        }
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
        List<Integer> current = new ArrayList<>(passed.size());

        for (Entry entry : passed) {
            int score = entry.getPosition();
            Team team = scoreboard.getTeam(TEAM_PREFIX + score);
            String temp = getEntry(score);

            if (!scoreboard.getEntries().contains(temp)) {
                objective.getScore(temp).setScore(score);
            }

            String[] splitContext = splitContextIntoTwo(entry.getContext());

            team.setPrefix(Strings.format(splitContext[0]));
            team.setSuffix(Strings.format(splitContext[1]));

            current.add(score);
        }

        Set<String> scoreboardEntries = scoreboard.getEntries();

        for (int slot = 1; slot < 16; slot++) {
            if (current.contains(slot)) {
                continue;
            }

            String entry = getEntry(slot);

            if (scoreboardEntries.contains(entry)) {
                scoreboard.resetScores(entry);
            }
        }
    }

    private String[] splitContextIntoTwo(String context) {
        if (context.length() <= 64) {
            return new String[] {context, ""};
        }

        int cutIndex = 64;
        String lastHex = "";
        Matcher matcher = StringMatcher.HEX_PATTERN.matcher(context);

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            if (cutIndex >= start && cutIndex < end) {
                cutIndex = start;
                break;
            }

            if (start < cutIndex) {
                lastHex = matcher.group(0);
            }
        }

        String prefix = context.substring(0, cutIndex);
        String suffix = lastHex + context.substring(cutIndex);
        return new String[] {prefix, suffix};
    }

    private String getEntry(int slot) {
        return chatColors[slot].toString();
    }
}
