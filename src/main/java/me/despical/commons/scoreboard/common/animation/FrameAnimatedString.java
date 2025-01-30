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

import me.despical.commons.util.Collections;

import java.util.List;

/**
 * @author Despical
 * <p>
 * Created at 22.05.2021
 */
public class FrameAnimatedString implements AnimatableString {

	protected List<String> frames;
	protected int currentFrame = -1;

	public FrameAnimatedString(String... frames) {
		this.frames = Collections.listOf(frames);
	}

	public FrameAnimatedString(List<String> frames) {
		this.frames = frames;
	}

	public void addFrame(String string) {
		frames.add(string);
	}

	public void setFrame(int frame, String string) {
		frames.set(frame, string);
	}

	public void removeFrame(String string) {
		frames.remove(string);
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public int getTotalLength() {
		return frames.size();
	}

	public String getString(int frame) {
		return frames.get(frame);
	}

	@Override
	public String current() {
		return currentFrame == -1 ? null : frames.get(currentFrame);
	}

	@Override
	public String next() {
		if (++currentFrame == frames.size()) currentFrame = 0;
		return frames.get(currentFrame);
	}

	@Override
	public String previous() {
		if (--currentFrame == -1) currentFrame = frames.size() - 1;
		return frames.get(currentFrame);
	}
}
