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