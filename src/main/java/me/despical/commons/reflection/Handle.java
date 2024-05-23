package me.despical.commons.reflection;

/**
 * <a href="https://github.com/CryptoMorin/XSeries/tree/master/src/main/java/com/cryptomorin/xseries/reflection">Original Source Code</a>
 *
 * @author CryptoMorin
 * @author Despical
 * <p>
 * Created at 23.05.2024
 */
public interface Handle<T> {

	default boolean exists() {
		try {
			reflect();
			return true;
		} catch (ReflectiveOperationException ignored) {
			return false;
		}
	}

	default ReflectiveOperationException catchError() {
		try {
			reflect();
			return null;
		} catch (ReflectiveOperationException ex) {
			return ex;
		}
	}

	default T unreflect() {
		try {
			return reflect();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	T reflect() throws ReflectiveOperationException;
}