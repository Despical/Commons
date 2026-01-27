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

package dev.despical.commons.scoreboard.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Despical
 * <p>
 * Created at 17.06.2020
 */
public final class EntryBuilder {

    private final List<Entry> entries = new LinkedList<>();

    public EntryBuilder next(String text) {
        return next(parse(text));
    }

    public EntryBuilder next(Component component) {
        entries.add(new Entry(component, entries.size()));
        return this;
    }

    public EntryBuilder blank() {
        return next(Component.empty());
    }

    public List<Entry> build() {
        int count = entries.size();

        for (Entry entry : entries) {
            entry.setPosition(count - entry.getPosition());
        }

        return entries;
    }

    private static Component parse(String text) {
        if (text == null || text.isEmpty()) return Component.empty();

        if (text.contains("<") && text.contains(">")) {
            return MiniMessage.miniMessage().deserialize(text);
        }

        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }
}
