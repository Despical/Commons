package me.despical.commons.reflection.jvm;

import me.despical.commons.reflection.jvm.classes.ClassHandle;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <a href="https://github.com/CryptoMorin/XSeries/tree/master/src/main/java/com/cryptomorin/xseries/reflection">Original Source Code</a>
 *
 * @author CryptoMorin
 * @author Despical
 * <p>
 * Created at 23.05.2024
 */
public class ConstructorMemberHandle extends MemberHandle {

	protected Class<?>[] parameterTypes = new Class[0];

	public ConstructorMemberHandle(ClassHandle clazz) {
		super(clazz);
	}

	public ConstructorMemberHandle parameters(Class<?>... parameterTypes) {
		this.parameterTypes = parameterTypes;
		return this;
	}

	public ConstructorMemberHandle parameters(ClassHandle... parameterTypes) {
		this.parameterTypes = Arrays.stream(parameterTypes).map(ClassHandle::unreflect).toArray(Class[]::new);
		return this;
	}


	@Override
	public MethodHandle reflect() throws ReflectiveOperationException {
		if (isFinal) throw new UnsupportedOperationException("Constructor cannot be final: " + this);
		if (makeAccessible) {
			return lookup.unreflectConstructor(reflectJvm());
		} else {
			return lookup.findConstructor(clazz.unreflect(), MethodType.methodType(void.class, this.parameterTypes));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Constructor<?> reflectJvm() throws ReflectiveOperationException {
		return handleAccessible(clazz.unreflect().getDeclaredConstructor(parameterTypes));
	}

	@Override
	public String toString() {
		String str = this.getClass().getSimpleName() + '{';
		if (makeAccessible) str += "protected/private ";
		str += clazz.toString() + ' ';
		str += '(' + Arrays.stream(parameterTypes).map(Class::getSimpleName).collect(Collectors.joining(", ")) + ')';
		return str + '}';
	}
}