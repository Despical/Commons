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

import com.cryptomorin.xseries.reflection.XReflection;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Despical
 * <p>
 * Created at 17.06.2020
 */
public final class EntryBuilder {

    private static final int MAX_LENGTH = XReflection.supports(14) ? 144 : 48;

	private final List<Entry> entries = new LinkedList<>();

	public EntryBuilder blank() {
		return next("");
	}

	public EntryBuilder next(String context) {
		return next(context, entries.size());
	}

	public EntryBuilder next(String context, int position) {
		entries.add(new Entry(adaptEntryLength(context), position));
		return this;
	}

	public List<Entry> build() {
        int entryCount = entries.size();

		for (Entry entry : entries) {
			entry.setPosition(entryCount - entry.getPosition());
		}

		return entries;
	}

	public List<Entry> buildRaw() {
		return new LinkedList<>(entries);
	}

	private String adaptEntryLength(String entry) {
        if (entry.length() > MAX_LENGTH) {
            entry = entry.substring(0, MAX_LENGTH - 1);
        }

		return entry;
	}

    public static List<Entry> empty() {
        return Collections.emptyList();
    }
}
