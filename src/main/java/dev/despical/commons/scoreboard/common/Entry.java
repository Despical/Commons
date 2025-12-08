/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2025 Despical
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

package dev.despical.commons.scoreboard.common;

/**
 * @author Despical
 * <p>
 * Created at 17.06.2020
 */
public class Entry {

	private String context;
	private int position;

	public Entry(String context, int position) {
		this.context = context;
		this.position = position;
	}

	public String getContext() {
		return context;
	}

    public void setContext(String context) {
        this.context = context;
    }

    public int getPosition() {
		return position;
	}

    public void setPosition(int position) {
        this.position = position;
    }
}
