package me.despical.commons.miscellaneous;

import me.despical.commons.compat.VersionResolver;
import org.bukkit.block.Block;

/**
 * @author Despical
 * <p>
 * Created at 22.05.2021
 */
public class BlockUtils {

	private BlockUtils() {
	}

	public static void setData(Block block, byte data) {
		if (VersionResolver.isCurrentEqualOrHigher(VersionResolver.ServerVersion.v1_13_R1)) {
			return;
		}

		try {
			Block.class.getMethod("setData", byte.class).invoke(block, data);
		} catch (Exception ignored) {
			// Ignore exception
		}
	}
}