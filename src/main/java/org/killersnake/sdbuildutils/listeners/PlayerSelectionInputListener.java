package org.killersnake.sdbuildutils.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.killersnake.sdbuildutils.utils.SelectionManager;

public class PlayerSelectionInputListener implements Listener {

	private final SelectionManager selectionManager;

	public PlayerSelectionInputListener(SelectionManager selectionManager) {
		this.selectionManager = selectionManager;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (!event.getAction().isRightClick()) {
			return;
		}

		ItemStack item = event.getItem();

		if (item == null || item.getType() != Material.BLAZE_ROD) {
			return;
		}

		selectionManager.toggle(event.getPlayer());
	}
}
