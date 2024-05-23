package me.despical.commons.reflection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * <a href="https://github.com/CryptoMorin/XSeries/tree/master/src/main/java/com/cryptomorin/xseries/reflection">Original Source Code</a>
 *
 * @author CryptoMorin
 * @author Despical
 * <p>
 * Created at 23.05.2024
 */
public class AggregateHandle<T, H extends Handle<T>> implements Handle<T> {

	private final List<H> handles;
	private Consumer<H> handleModifier;

	public AggregateHandle() {
		this.handles = new ArrayList<>(5);
	}

	public AggregateHandle(Collection<H> handles) {
		this.handles = new ArrayList<>(handles.size());
		this.handles.addAll(handles);
	}

	public AggregateHandle<T, H> or(H handle) {
		this.handles.add(handle);
		return this;
	}

	public AggregateHandle<T, H> modify(Consumer<H> handleModifier) {
		this.handleModifier = handleModifier;
		return this;
	}

	@Override
	public T reflect() throws ReflectiveOperationException {
		ClassNotFoundException errors = null;

		for (H handle : handles) {
			if (handleModifier != null) handleModifier.accept(handle);
			try {
				return handle.reflect();
			} catch (ClassNotFoundException ex) {
				if (errors == null)
					errors = new ClassNotFoundException("None of the aggregate handles were successful");
				errors.addSuppressed(ex);
			}
		}

		throw errors;
	}
}