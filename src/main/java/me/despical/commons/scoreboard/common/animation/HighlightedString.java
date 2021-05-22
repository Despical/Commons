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