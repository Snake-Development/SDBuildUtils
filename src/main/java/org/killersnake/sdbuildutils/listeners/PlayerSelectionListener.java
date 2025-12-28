package org.killersnake.sdbuildutils.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.killersnake.sdbuildutils.events.PlayerEndSelectionEvent;
import org.killersnake.sdbuildutils.events.PlayerStartSelectionEvent;
import org.killersnake.sdbuildutils.utils.Utils;

public class PlayerSelectionListener implements Listener {

	@EventHandler
	public void onStart(PlayerStartSelectionEvent event) {
		Player player = event.getPlayer();
		Location location = event.getLocation();

		Utils.messagePlayer(player.getName(), "Selection started at (%.2f %.2f %.2f)".formatted(location.x(), location.y(), location.z()));
	}

	@EventHandler
	public void onEnd(PlayerEndSelectionEvent event) {
		Player player = event.getPlayer();
		Location location = event.getLocation();

		Utils.messagePlayer(player.getName(), "Selection ended at (%.2f %.2f %.2f)".formatted(location.x(), location.y(), location.z()));
	}
}
