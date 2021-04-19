package me.despical.commons.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static me.despical.commons.util.Collections.*;

/**
 * @author Despical
 * <p>
 * Created at 4.04.2021
 */
public class CollectionsTest {

	@Test
	public void listCreations() {
		assertEquals(listOf(1, 2, 3), Arrays.asList(1, 2, 3));
		assertEquals(listOf("ABC", "DEF", "GHI"), Arrays.asList("ABC", "DEF", "GHI"));
		assertEquals(listOf('a', 'b', 'c'), Arrays.asList('a', 'b', 'c'));
	}

	@Test
	public void setCreations() {
		assertEquals(setOf(1, 2, 3), new HashSet<>(Arrays.asList(1, 2, 3)));
		assertEquals(setOf("ABC", "DEF", "GHI"), new HashSet<>(Arrays.asList("ABC", "DEF", "GHI")));
		assertEquals(setOf('a', 'b', 'c'), new HashSet<>(Arrays.asList('a', 'b', 'c')));
	}

	@Test
	public void mapCreations() {
		assertEquals(mapOf(1, "ABC"), java.util.Collections.singletonMap(1, "ABC"));
		assertEquals(mapOf('c', "C"), java.util.Collections.singletonMap('c', "C"));
		assertEquals(mapOf(.1, "0.1"), java.util.Collections.singletonMap(.1, "0.1"));
	}

	@Test
	public void listCreationsFromSet() {
		assertEquals(listFromSet(setOf("A", "B", "C")), listOf("A", "B", "C"));
		assertEquals(listFromSet(setOf('A', 'B', 'C')), listOf('A', 'B', 'C'));
		assertEquals(listFromSet(setOf("A", "B", "C")), listOf("A", "B", "C"));
	}

	@Test
	public void listCreationsFromMap() {
		// Collections#listFromMap returns list that contains map entry so we also need to
		// make list that contains same type, there is no difference creating from abstract
		// map entry or from Collections#mapEntry; both of them are same.
		assertEquals(listFromMap(mapOf("A", 1)), listOf(mapEntry("A", 1)));
		assertEquals(listFromMap(mapOf('B', "b")), listOf(mapEntry('B', "b")));
		assertEquals(listFromMap(mapOf(1, 'C')), listOf(mapEntry(1, 'C')));
	}
}