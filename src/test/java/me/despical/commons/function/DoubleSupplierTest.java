package me.despical.commons.function;

import me.despical.commons.util.function.DoubleSupplier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Despical
 * <p>
 * Created at 1.10.2022
 */
public class DoubleSupplierTest {

	@Test
	public void serializeInt() {
		String testString = "Commons is made by Despical";

		DoubleSupplier<String, Boolean> booleanSupplier = s -> !s.isEmpty() && s.contains(" ");
		DoubleSupplier<String, Integer> integerSupplier = String::length;

		assertEquals(true, booleanSupplier.accept(testString));
		assertEquals(java.util.Optional.of(27).get(), integerSupplier.accept(testString));
	}
}