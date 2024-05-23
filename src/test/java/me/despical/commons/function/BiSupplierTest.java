/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2023 Despical
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

package me.despical.commons.function;

import me.despical.commons.util.function.BiSupplier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Despical
 * <p>
 * Created at 1.10.2022
 */
public class BiSupplierTest {

	@Test
	public void serializeInt() {
		String testString = "Commons is made by Despical";

		BiSupplier<String, Boolean> booleanSupplier = s -> !s.isEmpty() && s.contains(" ");
		BiSupplier<String, Integer> integerSupplier = String::length;

		assertEquals(true, booleanSupplier.accept(testString));
		assertEquals(java.util.Optional.of(27).get(), integerSupplier.accept(testString));
	}
}