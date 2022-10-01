package me.despical.commons.util.function;

import java.util.Objects;

/**
 * @author Despical
 * <p>
 * Created at 1.10.2022
 */
@FunctionalInterface
public interface DoubleSupplier<T, R> {

	R accept(T t);

	default DoubleSupplier<T, R> andThen(final DoubleSupplier<T, R> doubleSupplier) {
		Objects.requireNonNull(doubleSupplier);

		return (t) -> {
			this.accept(t);
			return doubleSupplier.accept(t);
		};
	}
}