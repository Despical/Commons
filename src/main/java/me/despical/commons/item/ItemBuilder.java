/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2024 Despical
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

package me.despical.commons.item;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.reflection.XReflection;
import com.google.common.collect.Multimap;

import me.despical.commons.util.Collections;
import me.despical.commons.util.Strings;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class ItemBuilder {

	private final ItemStack itemStack;

	public ItemBuilder(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public ItemBuilder(Material material) {
		this.itemStack = new ItemStack(material);
	}

	public ItemBuilder(XMaterial xmaterial) {
		this.itemStack = xmaterial.parseItem();
	}

	public ItemBuilder(Optional<XMaterial> xmaterial) {
		this.itemStack = xmaterial.orElseThrow(NullPointerException::new).parseItem();
	}

	public ItemBuilder type(Material material) {
		itemStack.setType(material);
		return this;
	}

	public ItemBuilder amount(int amount) {
		itemStack.setAmount(amount);
		return this;
	}

	public ItemBuilder data(byte data) {
		itemStack.getData().setData(data);
		return this;
	}

	public ItemBuilder name(String name) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(Strings.format(name));

		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder enchantment(Enchantment enchantment) {
		itemStack.addUnsafeEnchantment(enchantment, 1);
		return this;
	}

	public ItemBuilder enchantment(Enchantment enchantment, int level) {
		this.itemStack.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder enchantment(XEnchantment xenchantment) {
		Optional.ofNullable(xenchantment.getEnchant()).ifPresent(enchantment -> itemStack.addUnsafeEnchantment(enchantment, 1));
		return this;
	}

	public ItemBuilder enchantment(XEnchantment xenchantment, int level) {
		Optional.ofNullable(xenchantment.getEnchant()).ifPresent(enchantment -> itemStack.addUnsafeEnchantment(enchantment, level));
		return this;
	}

	public ItemBuilder flag(ItemFlag... flags) {
		Stream.of(flags).forEach(this::flag);
		return this;
	}

	public ItemBuilder flag(ItemFlag itemFlag) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.addItemFlags(itemFlag);

		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder durability(short durability) {
		itemStack.setDurability(durability);
		return this;
	}

	public ItemBuilder unbreakable(boolean unbreakable) {
		ItemMeta itemMeta = itemStack.getItemMeta();

		if (XReflection.supports(9)) {
			itemMeta.setUnbreakable(unbreakable);
		} else {
			try {
				Method instanceMethod = itemMeta.getClass().getMethod("spigot");
				instanceMethod.setAccessible(true);

				Object instance = instanceMethod.invoke(itemMeta);
				Method unbreakableMethod = instance.getClass().getMethod("setUnbreakable", boolean.class);
				unbreakableMethod.setAccessible(true);
				unbreakableMethod.invoke(instance, unbreakable);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		itemStack.setItemMeta(itemMeta);
		return this;
	}

	public ItemBuilder hideTooltip() {
		ItemMeta meta = itemStack.getItemMeta();

		if (XReflection.supports(20, 5)) {
			try {
				Method getDefaultAttributeModifiers = Material.class.getMethod("getDefaultAttributeModifiers", EquipmentSlot.class);
				getDefaultAttributeModifiers.setAccessible(true);

				Multimap<Attribute, AttributeModifier> defaultAttributes = (Multimap<Attribute, AttributeModifier>) getDefaultAttributeModifiers.invoke(itemStack.getType(), EquipmentSlot.HAND);
				meta.setAttributeModifiers(defaultAttributes);
			} catch (Throwable ignored) {
				ignored.printStackTrace();
			}
		}

		meta.addItemFlags(ItemFlag.values());

		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder glow(boolean glow) {
		if (!glow) return this;

		return this.enchantment(XEnchantment.UNBREAKING.getEnchant()).flag(ItemFlag.HIDE_ENCHANTS);
	}

	public ItemBuilder glow() {
		return this.glow(true);
	}

	public ItemBuilder lore(String... lore) {
		return lore(Collections.listOf(lore));
	}

	public ItemBuilder loreIf(boolean condition, String... lore) {
		if (condition) {
			this.lore(lore);
		}

		return this;
	}

	public ItemBuilder lore(List<String> loreList) {
		ItemMeta meta = itemStack.getItemMeta();
		List<String> lore = meta.getLore();

		if (lore == null) {
			lore = new ArrayList<>();
		}

		lore.addAll(loreList);
		meta.setLore(lore.stream().map(Strings::format).collect(Collectors.toList()));

		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemStack build() {
		return itemStack;
	}
}