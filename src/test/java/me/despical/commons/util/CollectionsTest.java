/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2025 Despical
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

package me.despical.commons.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static me.despical.commons.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;

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

	@Test
	public void arrayContains() {
		assertFalse(contains("test", "best", "east", "nest"));
		assertFalse(contains('a', 'A', 'b', 'c'));
		assertTrue(contains(listOf(), listOf(), listOf(1), listOf(0.)));
		assertTrue(contains(.1, 0.1, 0., .0));
	}
}