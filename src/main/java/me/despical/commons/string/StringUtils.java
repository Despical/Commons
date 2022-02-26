package me.despical.commons.string;

/**
 * @author Despical
 * <p>
 * Created at 26.02.2022
 */
public class StringUtils {

	public static String capitalize(final String string, final char... delimiters) {
		char[] chars = string.toCharArray();
		boolean capitalizeNext = true; // to make first char upper case

		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];

			if (isDelimiter(c, delimiters)) {
				capitalizeNext = true;;
			} else if (capitalizeNext) {
				chars[i] = Character.toTitleCase(c);
				capitalizeNext = false;
			}
		}

		return new String(chars);
	}

	private static boolean isDelimiter(final char c, final char... delimiters) {
		if (delimiters == null) return Character.isWhitespace(c);

		for (char delimiter : delimiters) {
			if (c == delimiter) {
				return true;
			}
		}

		return false;
	}
}