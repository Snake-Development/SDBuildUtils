package org.killersnake.sdbuildutils.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import java.util.Arrays;

public class Utils {

	public static void messagePlayer(String name, String message) {
		Bukkit.getPlayer(name).sendMessage(Component.text("[SDBuildUtils] " + message));
	}
	public static boolean isBlacklisted(Material material) {
		final Material[] blacklistedMaterials = {
			Material.SCULK_SHRIEKER
		};
		return Arrays.stream(blacklistedMaterials).anyMatch(mat -> mat == material);
	}
}
