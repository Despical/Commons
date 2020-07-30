package me.despical.commonsbox.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class StringMatcher {

	private StringMatcher() {}

	public static List<Match> match(String base, List<String> possibilities) {
		possibilities.sort((o1, o2) -> {
			if (o1.length() == o2.length()) {
				return 0;
			}
			return Integer.compare(o2.length(), o1.length());
		});

		int baseLength = base.length();
		Match bestMatch = new Match(base, -1);
		List<Match> otherMatches = new ArrayList<>();

		for (String poss : possibilities) {
			if (!poss.isEmpty()) {
				int matches = 0;
				int pos = -1;
				for (int i = 0; i < Math.min(baseLength, poss.length()); i++) {
					if (base.charAt(i) == poss.charAt(i)) {
						if (pos != -1) {
							break;
						}
						pos = i;
					}
				}

				for (int i = 0; i < Math.min(baseLength, poss.length()); i++) {
					if ((pos != -1)
						&& (base.charAt(i) == poss.charAt(Math.min(i + pos, poss.length() - 1)))) {
						matches++;
					}
				}

				if (matches > bestMatch.length) {
					bestMatch = new Match(poss, matches);
				}

				if ((matches > 0) && (matches >= bestMatch.length)
					&& (!poss.equalsIgnoreCase(bestMatch.match))) {
					otherMatches.add(new Match(poss, matches));
				}
			}
		}
		otherMatches.add(bestMatch);
		Collections.sort(otherMatches);
		return otherMatches;
	}

	public static class Match implements Comparable<Match> {
		protected final String match;
		protected final int length;

		protected Match(String s, int i) {
			this.match = s;
			this.length = i;
		}

		public String getMatch() {
			return this.match;
		}

		public int compareTo(Match other) {
			return Integer.compare(other.length, this.length);
		}
	}
}