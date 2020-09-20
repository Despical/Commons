package me.despical.commonsbox.sorter;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class SortUtils {

	public static Map sortByValue(Map unsortMap) {
		List list = new LinkedList(unsortMap.entrySet());
		list.sort((o1, o2) -> ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue()));
		Map sortedMap = new LinkedHashMap();

		for (Object aList : list) {
			Map.Entry entry = (Map.Entry) aList;
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
}