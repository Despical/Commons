package me.despical.commons.miscellaneous;

import com.cryptomorin.xseries.XPotion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Despical
 * <p>
 * Created at 16.02.2025
 */
public class PotionUtils {

	private PotionUtils() {
	}

	public static PotionEffect hideEffect(PotionEffect potion) {
		return createPotionEffect(potion.getType(), potion.getDuration(), potion.getAmplifier());
	}

	public static PotionEffect hideEffect(XPotion potion, int duration, int amplifier) {
		return createPotionEffect(potion.getPotionEffectType(), duration, amplifier);
	}

	private static PotionEffect createPotionEffect(PotionEffectType type, int duration, int amplifier) {
		try {
			return new PotionEffect(type, duration, amplifier, false, false, false);
		} catch (Throwable ignored) {
			return new PotionEffect(type, duration, amplifier, false, false);
		}
	}
}