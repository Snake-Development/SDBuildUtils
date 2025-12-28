package org.killersnake.sdbuildutils.events;


import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerEndSelectionEvent extends Event implements Cancellable {

	private final Player player;
	private final Location location;

	private static final HandlerList HANDLER_LIST = new HandlerList();
	private boolean cancelled = false;

	public PlayerEndSelectionEvent(Player player, Location location) {
		this.player = player;
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public Player getPlayer() {
		return player;
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLER_LIST;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
