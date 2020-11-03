package me.despical.commonsbox.miscellaneous;

import me.despical.commonsbox.compat.VersionResolver;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Despical
 * @since 1.1.9
 * <p>
 * Created at 03.11.2020
 */
public class PlayerUtils {

	public static void hidePlayer(Player to, Player p, JavaPlugin plugin) {
		if (VersionResolver.isCurrentEqualOrHigher(VersionResolver.ServerVersion.v1_13_R1)) {
			to.hidePlayer(plugin, p);
		} else {
			to.hidePlayer(p);
		}
	}

	public static void showPlayer(Player to, Player p, JavaPlugin plugin) {
		if (VersionResolver.isCurrentEqualOrHigher(VersionResolver.ServerVersion.v1_13_R1)) {
			to.showPlayer(plugin, p);
		} else {
			to.showPlayer(p);
		}
	}
}
