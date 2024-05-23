package me.despical.commons.reflection.jvm.classes;

import java.lang.reflect.Array;

/**
 * <a href="https://github.com/CryptoMorin/XSeries/tree/master/src/main/java/com/cryptomorin/xseries/reflection">Original Source Code</a>
 *
 * @author CryptoMorin
 * @author Despical
 * <p>
 * Created at 23.05.2024
 */
public class StaticClassHandle extends ClassHandle {

	protected Class<?> clazz;

	public StaticClassHandle(Class<?> clazz) {
		this.clazz = clazz;
	}

	private Class<?> purifyClass() {
		Class<?> pureClazz = null;

		while (true) {
			Class<?> component = clazz.getComponentType();

			if (component != null)
				pureClazz = component;
			else
				break;
		}

		return pureClazz;
	}

	public StaticClassHandle asArray(int dimension) {
		Class<?> arrayClass = purifyClass();
		for (int i = 0; i < dimension; i++) {
			arrayClass = Array.newInstance(arrayClass, 0).getClass();
		}
		this.clazz = arrayClass;
		return this;
	}

	@Override
	public Class<?> reflect() throws ClassNotFoundException {
		return this.clazz;
	}

	@Override
	public boolean isArray() {
		return clazz.isArray();
	}
}