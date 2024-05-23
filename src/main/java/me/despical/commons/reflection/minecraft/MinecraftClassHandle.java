package me.despical.commons.reflection.minecraft;

import me.despical.commons.reflection.XReflection;
import me.despical.commons.reflection.jvm.classes.DynamicClassHandle;

/**
 * <a href="https://github.com/CryptoMorin/XSeries/tree/master/src/main/java/com/cryptomorin/xseries/reflection">Original Source Code</a>
 *
 * @author CryptoMorin
 * @author Despical
 * <p>
 * Created at 23.05.2024
 */
public class MinecraftClassHandle extends DynamicClassHandle {

	public MinecraftClassHandle inPackage(MinecraftPackage minecraftPackage) {
		return inPackage(minecraftPackage, "");
	}

	public MinecraftClassHandle inPackage(MinecraftPackage minecraftPackage, String packageName) {
		this.packageName = minecraftPackage.getPackageId();
		if (!packageName.isEmpty() && (minecraftPackage != MinecraftPackage.NMS || XReflection.supports(17))) {
			this.packageName += '.' + packageName;
		}
		return this;
	}

	public MinecraftClassHandle named(String... clazzNames) {
		super.named(clazzNames);
		return this;
	}

	public MinecraftClassHandle map(MinecraftMapping mapping, String className) {
		this.classNames.add(className);
		return this;
	}
}