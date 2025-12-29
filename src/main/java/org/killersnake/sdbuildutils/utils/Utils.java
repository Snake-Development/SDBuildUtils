package org.killersnake.sdbuildutils.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class Utils {

	public static void messagePlayer(String name, String message) {
		Bukkit.getPlayer(name).sendMessage(Component.text("[SDBuildUtils] " + message));
	}

}
