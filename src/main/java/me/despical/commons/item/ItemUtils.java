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

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.despical.commons.compat.XMaterial;
import me.despical.commons.reflection.XReflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

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

	public static ItemStack getSkull(String url) {
		ItemStack head = PLAYER_HEAD_ITEM.clone();

		if (url.isEmpty()) {
			return head;
		}

		SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), XReflection.supports(20) ? "" : null);
		profile.getProperties().put("textures", new Property("textures", url));

		if (XReflection.supports(21, 1)) {
			UUID uuid = UUID.randomUUID();
			PlayerProfile playerProfile = Bukkit.getServer().createProfile(UUID.randomUUID(), uuid.toString().substring(0, 16));
			playerProfile.setProperty(new ProfileProperty("textures", url));

			skullMeta.setPlayerProfile(playerProfile);
		} else if (XReflection.supports(15)) {
			try {
				Method method = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
				method.setAccessible(true);
				method.invoke(skullMeta, profile);
			} catch (Exception ignored) {
			}
		} else {
			try {
				Field profileField = skullMeta.getClass().getDeclaredField("profile");
				profileField.setAccessible(true);
				profileField.set(skullMeta, profile);
			} catch (Exception ignored) {
			}
		}

		head.setItemMeta(skullMeta);
		return head;
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