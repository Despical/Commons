/*
 * CommonsBox - Library box of common utilities.
 * Copyright (C) 2020 Despical
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

package me.despical.commonsbox.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class ItemBuilder {

	private final ItemStack itemStack;

	public ItemBuilder(final ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public ItemBuilder(final Material material) {
		this.itemStack = new ItemStack(material);
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
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

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

	public ItemBuilder flag(ItemFlag... flags) {
		flag(flags);
		return this;
	}

	public ItemBuilder flag(ItemFlag itemFlag) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.addItemFlags(itemFlag);

		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder unbreakable(boolean unbreakable) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.setUnbreakable(unbreakable);

		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder lore(String... name) {
		return lore(Arrays.asList(name));
	}

	public ItemBuilder lore(List<String> name) {
		ItemMeta meta = itemStack.getItemMeta();
		List<String> lore = meta.getLore();

		if (lore == null) {
			lore = new ArrayList<>();
		}

		lore.addAll(name);
		meta.setLore(lore.stream().map(str -> ChatColor.translateAlternateColorCodes('&', str)).collect(Collectors.toList()));

		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemStack build() {
		return itemStack;
	}
}