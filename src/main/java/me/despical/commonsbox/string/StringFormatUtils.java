/*
 * Commons Box - Box of common utilities.
 * Copyright (C) 2020 Despical
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package me.despical.commonsbox.string;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class StringFormatUtils {

	private StringFormatUtils() {}

	public static String formatIntoMMSS(int secsIn) {
		int minutes = secsIn / 60;
		int seconds = secsIn % 60;
		return ((minutes < 10 ? "0" : "") + minutes
			+ ":" + (seconds < 10 ? "0" : "") + seconds);
	}

	public static String getProgressBar(int current, int max, int totalBars, String symbol, String completedCharacter, String notCompletedCharacter) {
		float percent = (float) current / max;
		int progressBars = (int) (totalBars * percent);
		int leftOver = (totalBars - progressBars);
		StringBuilder sb = new StringBuilder();

		sb.append(completedCharacter);

		for (int i = 0; i < progressBars; i++) {
			sb.append(symbol);
		}

		sb.append(notCompletedCharacter);

		for (int i = 0; i < leftOver; i++) {
			sb.append(symbol);
		}

		return sb.toString();
	}
}