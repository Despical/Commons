/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2023 Despical
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

package me.despical.commons.scoreboard.common;

import java.util.List;
import java.util.LinkedList;

import me.despical.commons.ReflectionUtils;
import me.despical.commons.scoreboard.type.Entry;
import me.despical.commons.util.Strings;

/**
 * @author Despical
 * <p>
 * Created at 17.06.2020
 */
public final class EntryBuilder {

	private final List<Entry> entries = new LinkedList<>();

	public EntryBuilder blank() {
		return next("");
	}

	public EntryBuilder next(String string) {
		return next(string, entries.size());
	}

	public EntryBuilder next(String string, int position) {
		entries.add(new Entry(adapt(string), position));
		return this;
	}

	public List<Entry> build() {
		for (Entry entry : entries) {
			entry.setPosition(entries.size() - entry.getPosition());
		}

		return entries;
	}

	public List<Entry> buildRaw() {
		return new LinkedList<>(entries);
	}

	private String adapt(String entry) {
		if (ReflectionUtils.supports(14)) {
			if (entry.length() > 144) entry = entry.substring(0, 143);
		} else {
			if (entry.length() > 48) entry = entry.substring(0, 47);
		}

		return Strings.format(entry);
	}
}