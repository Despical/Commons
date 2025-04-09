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

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.reflection.XReflection;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
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
		ItemStack head = PLAYER_HEAD_ITEM.clone();

		if (url.isEmpty()) {
			return head;
		}

		SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), XReflection.supports(20) ? "" : null);
		profile.getProperties().put("textures", new Property("textures", url));

		if (XReflection.supports(21, 1)) {
			UUID uuid = UUID.randomUUID();

			try {
				PlayerProfile playerProfile = Bukkit.getServer().createProfile(UUID.randomUUID(), uuid.toString().substring(0, 16));
				playerProfile.setProperty(new ProfileProperty("textures", url));

				skullMeta.setPlayerProfile(playerProfile);
			} catch (Throwable ignored) {
				try {
					Method CREATE_PLAYER_PROFILE = Bukkit.getServer().getClass().getMethod("createPlayerProfile", String.class);
					CREATE_PLAYER_PROFILE.setAccessible(true);
					Object BUKKIT_PROFILE = CREATE_PLAYER_PROFILE.invoke(Bukkit.getServer(), uuid.toString().substring(0, 16));

					Method GET_TEXTURES = BUKKIT_PROFILE.getClass().getMethod("getTextures");
					GET_TEXTURES.setAccessible(true);
					Object TEXTURES = GET_TEXTURES.invoke(BUKKIT_PROFILE);

					String base64Value = new String(Base64.getDecoder().decode(url));
					JsonObject jsonObject = new JsonParser().parse(base64Value).getAsJsonObject();
					String newUrl = jsonObject
						.getAsJsonObject("textures")
						.getAsJsonObject("SKIN")
						.get("url")
						.getAsString();

					Method SET_SKIN = TEXTURES.getClass().getMethod("setSkin", URL.class);
					SET_SKIN.setAccessible(true);
					SET_SKIN.invoke(TEXTURES, new URL("https://textures.minecraft.net/texture/" + newUrl));

					Method SET_TEXTURES = Arrays.stream(BUKKIT_PROFILE.getClass().getMethods()).filter(method -> method.getName().equals("setTextures")).findFirst().get();
					SET_TEXTURES.setAccessible(true);
					SET_TEXTURES.invoke(BUKKIT_PROFILE, TEXTURES);

					Method SET_OWNER_PROFILE = Arrays.stream(skullMeta.getClass().getMethods()).filter(method -> method.getName().equals("setOwnerProfile")).findFirst().get();
					SET_OWNER_PROFILE.setAccessible(true);
					SET_OWNER_PROFILE.invoke(skullMeta, BUKKIT_PROFILE);
				} catch (Throwable ignored1) {
				}
			}
		}

		if (XReflection.supports(15)) {
			try {
				Method method = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
				method.setAccessible(true);
				method.invoke(skullMeta, profile);
			} catch (Throwable ignored) {
			}
		} else {
			try {
				Field profileField = skullMeta.getClass().getDeclaredField("profile");
				profileField.setAccessible(true);
				profileField.set(skullMeta, profile);
			} catch (Throwable ignored) {
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