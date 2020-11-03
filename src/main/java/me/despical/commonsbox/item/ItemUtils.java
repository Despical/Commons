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

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import me.despical.commonsbox.compat.VersionResolver;
import me.despical.commonsbox.compat.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

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

	private ItemUtils() {}

	public static boolean isNamed(ItemStack stack) {
		if (stack == null) {
			return false;
		}

		return stack.hasItemMeta() && stack.getItemMeta().hasDisplayName();
	}

	public static ItemStack getSkull(String url) {
		ItemStack head = PLAYER_HEAD_ITEM.clone();

		if (url.isEmpty()) {
			return head;
		}

		SkullMeta headMeta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);

		profile.getProperties().put("textures", new Property("textures", url));

		Field profileField;

		try {
			profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, profile);
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {}

		head.setItemMeta(headMeta);
		return head;
	}

	public static SkullMeta setPlayerHead(Player player, SkullMeta meta) {
		if (Bukkit.getServer().getVersion().contains("Paper") && player.getPlayerProfile().hasTextures()) {
			return CompletableFuture.supplyAsync(() -> {
				meta.setPlayerProfile(player.getPlayerProfile());
				return meta;
			}).exceptionally(e -> meta).join();
		}

		if (VersionResolver.isCurrentHigher(VersionResolver.ServerVersion.v1_12_R1)) {
			meta.setOwningPlayer(player);
		} else {
			meta.setOwner(player.getName());
		}

		return meta;
	}
}