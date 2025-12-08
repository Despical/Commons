/*
 * Commons - Box of the common utilities.
 * Copyright (C) 2025 Despical
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

package dev.despical.commons.item;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import com.cryptomorin.xseries.reflection.XReflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class ItemUtils {

	public static final ItemStack PLAYER_HEAD_ITEM;

	static {
		PLAYER_HEAD_ITEM = XMaterial.PLAYER_HEAD.parseItem();
	}

	private ItemUtils() {
	}

	public static boolean isNamed(ItemStack stack) {
		return stack != null && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName();
	}

	/**
	 * Checks if the given 2 items have the same display name, lore and amount.
	 *
	 * @param first the first item to check
	 * @param second the second item to check
	 * @return {@code true} if the given 2 items are equals and both are same amount,
	 * 			otherwise {@code false}
	 */
	public static boolean isSameItems(ItemStack first, ItemStack second) {
		return isSameItemsWithoutAmount(first, second) && first.getAmount() == second.getAmount();
	}

	/**
	 * Checks if the given 2 items have the same display name and lore.
	 *
	 * @param first the first item to check
	 * @param second the second item to check
	 * @return {@code true} if the given 2 items are equals, may not be same amount
	 */
	public static boolean isSameItemsWithoutAmount(ItemStack first, ItemStack second) {
		if (first == null || second == null) {
			return false;
		}

		if (first.getType() != second.getType()) {
			return false;
		}

		ItemMeta firstMeta = first.getItemMeta();
		ItemMeta secondMeta = second.getItemMeta();

		return Objects.equals(firstMeta.getDisplayName(), secondMeta.getDisplayName()) &&
			Objects.equals(firstMeta.getLore(), secondMeta.getLore());
	}

	public static ItemStack getSkull(String url) {
		return XSkull.of(PLAYER_HEAD_ITEM.clone())
			.profile(Profileable.detect(url))
			.applyAsync()
			.join();
	}

	public static SkullMeta setPlayerHead(Player player, SkullMeta meta) {
		if (XReflection.supports(12)) {
			meta.setOwningPlayer(player);
		} else if (Bukkit.getServer().getVersion().contains("Paper") && player.getPlayerProfile().hasTextures()) {
			meta.setPlayerProfile(player.getPlayerProfile());
		} else {
			meta.setOwner(player.getName());
		}

		return meta;
	}
}