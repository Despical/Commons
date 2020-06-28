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
		this.itemStack.setType(material);
		return this;
	}

	public ItemBuilder amount(int amount) {
		this.itemStack.setAmount(amount);
		return this;
	}

	public ItemBuilder data(byte data) {
		this.itemStack.getData().setData(data);
		return this;
	}

	public ItemBuilder name(final String name) {
		final ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder enchantment(Enchantment enchantment) {
		this.itemStack.addUnsafeEnchantment(enchantment, 1);
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
		ItemMeta meta = this.itemStack.getItemMeta();
		meta.addItemFlags(itemFlag);
		this.itemStack.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder lore(final String... name) {
		return lore(Arrays.asList(name));
	}
	
	public ItemBuilder lore(final List<String> name) {
		final ItemMeta meta = itemStack.getItemMeta();
		List<String> lore = meta.getLore();
		if (lore == null) {
			lore = new ArrayList<>();
		}
		lore.addAll(name);
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder colorize() {
		ItemMeta meta = this.itemStack.getItemMeta();
		if(meta.hasDisplayName()) {
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', meta.getDisplayName()));
		}
		if(meta.hasLore()) {
			meta.setLore(meta.getLore().stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));
		}
		return this;
	}
	
	public ItemStack build() {
		return itemStack;
	}
}