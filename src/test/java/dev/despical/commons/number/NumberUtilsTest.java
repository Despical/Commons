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

package dev.despical.commons.number;

import org.junit.jupiter.api.Test;

import static dev.despical.commons.number.NumberUtils.roundInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumberUtilsTest {

	@Test
	public void serializeInt() {
		assertEquals(9, roundInteger(3, 9));
		assertEquals(9, roundInteger(9, 9));
		assertEquals(27, roundInteger(24, 9));
		assertEquals(36, roundInteger(37, 9));
		assertEquals(45, roundInteger(43, 9));
	}
}