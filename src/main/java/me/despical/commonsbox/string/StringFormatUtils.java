package me.despical.commonsbox.string;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class StringFormatUtils {

	private StringFormatUtils() {
	}

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