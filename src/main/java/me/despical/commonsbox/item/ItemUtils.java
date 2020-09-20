package me.despical.commonsbox.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.despical.commonsbox.compat.VersionResolver;
import me.despical.commonsbox.compat.XMaterial;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * @author Despical
 * <p>
 * Created at 30.05.2020
 */
public class ItemUtils {

	public static final ItemStack PLAYER_HEAD_ITEM = VersionResolver.isBefore(VersionResolver.ServerVersion.v1_13_R1) ? new ItemStack(Material.SKULL_ITEM, 1, (short) 3) : XMaterial.PLAYER_HEAD.parseItem();

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
}