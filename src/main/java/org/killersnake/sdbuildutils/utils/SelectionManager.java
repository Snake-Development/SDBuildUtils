package org.killersnake.sdbuildutils.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.killersnake.sdbuildutils.SDBuildUtils;
import org.killersnake.sdbuildutils.events.PlayerEndSelectionEvent;
import org.killersnake.sdbuildutils.events.PlayerStartSelectionEvent;
import org.killersnake.sdbuildutils.tasks.PersistentParticleTask;
import org.killersnake.sdbuildutils.utils.Selection.ResizeFace;
import org.killersnake.sdbuildutils.utils.Selection.ResizeOperation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

//TODO: remove particle logic and move to using player.sendBlockUpdate() inside the SelectionClass
public class SelectionManager {

	private final HashMap<UUID, Selection> selections = new HashMap<>();
	private final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

	private final SDBuildUtils plugin;
	private BukkitTask task_particle_start_sel;
	private BukkitTask task_particle_end_sel;

	public SelectionManager(SDBuildUtils plugin) {
		this.plugin = plugin;
	}

	public void toggle(Player player) {
		if (selections.containsKey(player.getUniqueId())) {
			if (!selections.get(player.getUniqueId()).isComplete()) {
				end(player);
			} else {
				start(player, true);
			}
		} else {
			start(player, true);
		}
	}

	public void start(Player player, boolean previousRemoveMessage) {
		if (selections.containsKey(player.getUniqueId())) {
			selections.remove(player.getUniqueId());
			if (previousRemoveMessage) {
				Utils.messagePlayer(player.getName(), "Removed previous selection");
			}
		}
		selections.put(player.getUniqueId(), Selection.start(player.getLocation().toCenterLocation()));

		Bukkit.getPluginManager().callEvent(
				new PlayerStartSelectionEvent(player, player.getLocation().toCenterLocation())
		);

		this.resetParticles();

		this.task_particle_start_sel = scheduler.runTaskTimer(plugin, new PersistentParticleTask(plugin, player.getLocation().toCenterLocation(), player), 10, 10);
	}

	public void end(Player player) {
		Selection sel = selections.get(player.getUniqueId()).end(player.getLocation().toCenterLocation());
		selections.put(player.getUniqueId(), sel);

		Bukkit.getPluginManager().callEvent(
				new PlayerEndSelectionEvent(player, player.getLocation().toCenterLocation())
		);

		this.task_particle_end_sel = scheduler.runTaskTimer(plugin, new PersistentParticleTask(plugin, player.getLocation().toCenterLocation(), player), 10, 10);
	}

	public boolean isSelecting(Player player) {
		return selections.containsKey(player.getUniqueId());
	}

	public void resetParticles() {
		if (this.task_particle_start_sel != null && !this.task_particle_start_sel.isCancelled()) {
			this.task_particle_start_sel.cancel();
		}
		if (this.task_particle_end_sel != null && !this.task_particle_end_sel.isCancelled()) {
			this.task_particle_end_sel.cancel();
		}
	}

	public void reset(Player player) {
		selections.remove(player.getUniqueId());
		resetParticles();
	}

	public void resize(Player player, ResizeFace resizeFace, ResizeOperation resizeOperation, int amount) {
		//TODO: change to Selection.SelectionResizeContext
		Selection oldSelection = selections.get(player.getUniqueId());
		reset(player);
		selections.put(
				player.getUniqueId(),
				oldSelection
						.resize(resizeFace, resizeOperation, amount)
		);
	}

	public @Nullable Selection getSelection(Player player) {
		return selections.get(player.getUniqueId());
	}
}
