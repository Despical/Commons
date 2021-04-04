package me.despical.commons.util;

import java.util.*;

/**
 * @author Despical
 * <p>
 * Created at 4.04.2021
 */
public class Collections {

	private Collections() {
	}

	/**
	 * Returns an mutable list containing an arbitrary number of elements.
	 *
	 * @param a Array of given parameters.
	 * @param <T> Type of given parameters.
	 * @return mutable list containing an arbitrary number of elements.
	 */
	@SafeVarargs
	public static <T> List<T> listOf(T... a) {
		return new ArrayList<>(Arrays.asList(a));
	}

	/**
	 * Returns an immutable list containing an arbitrary number of elements.
	 *
	 * @param a Array of given parameters.
	 * @param <T> Type of given parameters.
	 * @return immutable list containing an arbitrary number of elements.
	 */
	@SafeVarargs
	public static <T> List<T> immutableListOf(T... a) {
		return Arrays.asList(a);
	}

	/**
	 * Returns an mutable set containing an arbitrary number of elements.
	 *
	 * @param a Array of given parameters.
	 * @param <T> Type of given parameters.
	 * @return mutable set containing an arbitrary number of elements.
	 */
	@SafeVarargs
	public static <T> Set<T> setOf(T... a) {
		return new HashSet<>(Arrays.asList(a));
	}

	/**
	 * Returns an immutable set containing an arbitrary number of elements.
	 *
	 * @param a Array of given parameters.
	 * @param <T> Type of given parameters.
	 * @return immutable set containing an arbitrary number of elements.
	 */
	@SafeVarargs
	public static <T> Set<T> immutableSetOf(T... a) {
		return java.util.Collections.unmodifiableSet(new HashSet<>(Arrays.asList(a)));
	}

	/**
	 * Returns a {@link Map.Entry} containing the given key and value.
	 *
	 * @param a new key to be stored in this entry.
	 * @param b new value to be stored in this entry.
	 * @param <K> new key type to be stored in this entry.
	 * @param <V> new value type to be stored in this entry.
	 * @return new {@link Map.Entry} containing the given key and value.
	 */
	public static <K, V> Map.Entry<K, V> mapEntry(K a, V b) {
		return new AbstractMap.SimpleEntry<>(a, b);
	}

	/**
	 * Returns an mutable map containing an arbitrary number of elements.
	 *
	 * @param a Array of given entries to be stored in this map.
	 * @param <K> key type to be stored in this map.
	 * @param <V> value type to be stored in this map.
	 * @return Returns an mutable map containing an arbitrary number of elements.
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> mapOf(Map.Entry<K, V>... a) {
		Map<K, V> map = new HashMap<>();

		for (Map.Entry<K, V> b : a) {
			map.put(b.getKey(), b.getValue());
		}

		return map;
	}

	/**
	 * Returns an mutable map containing one element.
	 *
	 * @param a key to be stored in this map.
	 * @param b value to be stored in this map.
	 * @param <K> key type to be stored in this map.
	 * @param <V> value type to be stored in this map.
	 * @return Returns an mutable map containing one element.
	 */
	public static <K, V> Map<K, V> mapOf(K a, V b) {
		return mapOf(mapEntry(a, b));
	}

	/**
	 * Returns an immutable map containing one element.
	 *
	 * @param a key to be stored in this map.
	 * @param b value to be stored in this map.
	 * @param <K> key type to be stored in this map.
	 * @param <V> value type to be stored in this map.
	 * @return Returns an immutable map containing one element.
	 */
	public static <K, V> Map<K, V> immutableMapOf(K a, V b) {
		return immutableMapOf(mapEntry(a, b));
	}

	/**
	 * Returns an immutable map containing an arbitrary number of elements.
	 *
	 * @param a Array of given entries to be stored in this map.
	 * @param <K> key type to be stored in this map.
	 * @param <V> value type to be stored in this map.
	 * @return immutable map containing an arbitrary number of elements.
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> immutableMapOf(Map.Entry<K, V>... a) {
		Map<K, V> map = new HashMap<>();

		for (Map.Entry<K, V> b : a) {
			map.put(b.getKey(), b.getValue());
		}

		return java.util.Collections.unmodifiableMap(map);
	}
}