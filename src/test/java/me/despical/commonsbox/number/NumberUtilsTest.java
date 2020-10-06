/*
 * CommonsBox - Library box of common utilities.
 * Copyright (C) 2020 Despical
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

package me.despical.commonsbox.number;

import org.junit.Assert;
import org.junit.Test;

public class NumberUtilsTest {

	@Test
	public void serializeInt() {
		Assert.assertEquals(9, NumberUtils.serializeInt(3));
		Assert.assertEquals(9, NumberUtils.serializeInt(9));
		Assert.assertEquals(27, NumberUtils.serializeInt(24));
		Assert.assertEquals(45, NumberUtils.serializeInt(37));
		Assert.assertEquals(45, NumberUtils.serializeInt(43));
	}
}
