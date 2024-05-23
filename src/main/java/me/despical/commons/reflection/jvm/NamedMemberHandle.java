package me.despical.commons.reflection.jvm;

import me.despical.commons.reflection.jvm.classes.ClassHandle;
import me.despical.commons.reflection.minecraft.MinecraftMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <a href="https://github.com/CryptoMorin/XSeries/tree/master/src/main/java/com/cryptomorin/xseries/reflection">Original Source Code</a>
 *
 * @author CryptoMorin
 * @author Despical
 * <p>
 * Created at 23.05.2024
 */
public abstract class NamedMemberHandle extends MemberHandle {

	protected Class<?> returnType;
	protected boolean isStatic;
	protected final List<String> names = new ArrayList<>(5);

	protected NamedMemberHandle(ClassHandle clazz) {
		super(clazz);
	}

	public NamedMemberHandle map(MinecraftMapping mapping, String name) {
		this.names.add(name);
		return this;
	}

	public NamedMemberHandle asStatic() {
		this.isStatic = true;
		return this;
	}

	public NamedMemberHandle returns(Class<?> clazz) {
		this.returnType = clazz;
		return this;
	}

	public NamedMemberHandle returns(ClassHandle clazz) {
		this.returnType = clazz.unreflect();
		return this;
	}

	public MemberHandle named(String... names) {
		this.names.addAll(Arrays.asList(names));
		return this;
	}
}