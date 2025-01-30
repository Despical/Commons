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

package me.despical.commons.scoreboard.common.animation;

/**
 * @author Despical
 * <p>
 * Created at 22.05.2021
 */
public class HighlightedString extends FrameAnimatedString {

	protected String context, normalFormat, highlightFormat, prefix, suffix;

	public HighlightedString(String context, String normalFormat, String highlightFormat) {
		this(context, normalFormat, highlightFormat, "", "");
	}

	public HighlightedString(String context, String normalFormat, String highlightFormat, String prefix, String suffix) {
		super();
		this.context = context;
		this.normalFormat = normalFormat;
		this.highlightFormat = highlightFormat;
		this.prefix = prefix;
		this.suffix = suffix;

		generateFrames();
	}

	protected void generateFrames() {
		int index = 0;

		while (index < context.length()) {
			if (context.charAt(index) != ' ') {
				addFrame(prefix + normalFormat + context.substring(0, index) + highlightFormat + context.charAt(index) + normalFormat + context.substring(index + 1) + suffix);
			} else {
				addFrame(prefix + normalFormat + context + suffix);
			}

			index++;
		}
	}

	public String getContext() {
		return context;
	}

	public String getNormalColor() {
		return normalFormat;
	}

	public String getHighlightColor() {
		return highlightFormat;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}
}
