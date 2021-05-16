package me.despical.commons.util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		return new ArrayList<>(immutableListOf(a));
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
	 * Returns an mutable list containing elements of given set.
	 *
	 * @param a Set to be converted to list.
	 * @param <T> Type of given set.
	 * @return mutable list containing elements of given set.
	 */
	public static <T> List<T> listFromSet(Set<T> a) {
		return new ArrayList<>(a);
	}

	/**
	 * Returns an immutable list containing elements of given set.
	 *
	 * @param a Set to be converted to list.
	 * @param <T> Type of given set.
	 * @return immutable list containing elements of given set.
	 */
	public static <T> List<T> immutableListFromSet(Set<T> a) {
		return java.util.Collections.unmodifiableList(listFromSet(a));
	}

	/**
	 * Returns an mutable list containing entry set elements of given map.
	 *
	 * @param a Map to be converted to list.
	 * @param <K> Type of entry.
	 * @param <V> Value of entry.
	 * @return mutable list containing entry set elements of given map.
	 */
	public static <K, V> List<Map.Entry<K, V>> listFromMap(Map<K, V> a) {
		return listFromSet(a.entrySet());
	}

	/**
	 * Returns an immutable list containing an arbitrary number of elements.
	 *
	 * @param a Map to be converted to list.
	 * @param <K> Type of entry.
	 * @param <V> Value of entry.
	 * @return immutable list containing entry set elements of given map.
	 */
	public static <K, V> List<Map.Entry<K, V>> immutableListFromMap(Map<K, V> a) {
		return java.util.Collections.unmodifiableList(listFromSet(a.entrySet()));
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
		return new HashSet<>(immutableListOf(a));
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
		return java.util.Collections.unmodifiableSet(setOf(a));
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
		return streamOf(a).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (b, c) -> c));
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
		return java.util.Collections.unmodifiableMap(mapOf(a));
	}

	/**
	 * Returns a sequential ordered stream whose elements are the specified values.
	 *
	 * @param <T> the type of stream elements
	 * @param a the elements of the new stream
	 * @return the new stream
	 */
	@SafeVarargs
	public static <T> Stream<T> streamOf(T... a) {
		return Arrays.stream(a);
	}

	/**
	 * Checks if the object is in the given array.
	 *
	 * The method returns false if a null array is passed in.
	 *
	 * @param objectToFind the object to find
	 * @param array the array to search through
	 * @return true if the array contains the object
	 */
	public static boolean contains(Object objectToFind, Object... array) {
		if (array == null) {
			return false;
		}

		if (objectToFind == null) {
			for (Object o : array) {
				if (o == null) {
					return true;
				}
			}
		} else if (array.getClass().getComponentType().isInstance(objectToFind)) {
			for (Object o : array) {
				if (objectToFind.equals(o)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Adds all of the elements in the specified collection to this collection
	 * (optional operation).  The behavior of this operation is undefined if
	 * the specified collection is modified while the operation is in progress.
	 * (This implies that the behavior of this call is undefined if the
	 * specified collection is this collection, and this collection is
	 * nonempty.)
	 *
	 * @param collection containing elements to be added to this collection
	 * @return <tt>true</tt> if this collection changed as a result of the call
	 * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
	 *         is not supported by this collection
	 * @throws ClassCastException if the class of an element of the specified
	 *         collection prevents it from being added to this collection
	 * @throws NullPointerException if the specified collection contains a
	 *         null element and this collection does not permit null elements,
	 *         or if the specified collection is null
	 * @throws IllegalArgumentException if some property of an element of the
	 *         specified collection prevents it from being added to this
	 *         collection
	 * @throws IllegalStateException if not all the elements can be added at
	 *         this time due to insertion restrictions
	 */
	@SafeVarargs
	public static <T> boolean addAll(Collection<T> collection, T... elements) {
		return collection.addAll(listOf(elements));
	}
}