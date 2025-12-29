package org.killersnake.sdbuildutils.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.killersnake.sdbuildutils.SDBuildUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Selection {
	private Location pos1;
	private Location pos2;

	private final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

	public static Selection start(Location pos1) {
		return new Selection(pos1, null);
	}

	public static Selection end(Selection sel, Location pos2) {
		return new Selection(sel.getPos1(), pos2);
	}

	public void fillSelection(Player player, BlockState blockState, long delayPerBlock) {
		SDBuildUtils plugin = (SDBuildUtils) Bukkit.getPluginManager().getPlugin("SDBuildUtils");
		World world = pos1.getWorld();

		if (world == null || !world.equals(pos2.getWorld())) {
			Utils.messagePlayer(
					player.getName(),
					"Error trying to fill the selection [%s]".formatted(this.toString())
			);
		};

		int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
		int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
		int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
		int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
		int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
		int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

		//TODO: make it take the items from inventory of player
			//TODO: ignore inventory if player has a specific permission (future feature)

		List<Block> blocks = new ArrayList<>();

		for (int y = minY; y <= maxY; y++) {
			for (int z = minZ; z <= maxZ; z++) {
				for (int x = minX; x <= maxX; x++) {
					blocks.add(world.getBlockAt(x, y, z));
				}
			}
		}

		Iterator<Block> iterator = blocks.iterator();

		new BukkitRunnable() {
			@Override
			public void run() {
				if (!iterator.hasNext()) {
					cancel();
					return;
				}
				Block block = iterator.next();
				block.setBlockData(blockState.getBlockData(), true);
			}
		}.runTaskTimer(plugin, 0L, delayPerBlock);

		String finishMessage = "Finished filling %s [%s]".formatted(blockState.getType().toString(), this.toString());

		new BukkitRunnable() {
			@Override
			public void run() {
				Utils.messagePlayer(
						player.getName(),
						finishMessage
				);
			}
		}.runTaskLater(plugin, blocks.toArray().length * delayPerBlock);
	}

	public Selection end(Location pos2) {
		return new Selection(pos1, pos2);
	}

	public Selection(Location pos1, Location pos2) {
		this.pos1 = pos1;
		this.pos2 = pos2;
	}

	public Location getPos1() {
		return pos1;
	}

	public Location getPos2() {
		return pos2;
	}

	public boolean isComplete() {
		return pos1 != null && pos2 != null;
	}

	public static enum ResizeFace {
		TOP,    // +Y
		BOTTOM, // -Y
		EAST,   // +X
		WEST,   // -X
		SOUTH,  // +Z
		NORTH   // -Z
	}
	public static enum ResizeOperation {
		INCREASE,
		DECREASE
	}

	public Selection resize(ResizeFace resizeFace, ResizeOperation resizeOperation, int amount) {
		int signedAmount = (resizeOperation == ResizeOperation.DECREASE) ? -amount : amount;
		Location p1 = pos1.clone();
		Location p2 = pos2.clone();
		switch (resizeFace) {
			case TOP -> {
				if (isPos1Top()) { p1.add(0, signedAmount, 0); } else { p2.add(0, signedAmount, 0); }
			}
			case BOTTOM -> {
				if (isPos1Bottom()) { p1.subtract(0, signedAmount, 0); } else { p2.subtract(0, signedAmount, 0); }
			}
			case EAST -> {
				if (isPos1East()) { p1.add(signedAmount, 0, 0); } else { p2.add(signedAmount, 0, 0); }
			}
			case WEST -> {
				if (isPos1West()) { p1.subtract(signedAmount, 0, 0); } else { p2.subtract(signedAmount, 0, 0); }
			}
			case SOUTH -> {
				if (isPos1South()) { p1.add(0, 0, signedAmount); } else { p2.add(0, 0, signedAmount); }
			}
			case NORTH -> {
				if (isPos1North()) { p1.subtract(0, 0, signedAmount); } else { p2.subtract(0, 0, signedAmount); }
			}
		}
		return new Selection(p1, p2);
	}

	//<editor-fold desc="private position checking methods">
	private boolean isPos1Top() {
		return pos1.y() > pos2.y();
	}
	private boolean isPos1Bottom() {
		return pos1.y() < pos2.y();
	}
	private boolean isPos1East() {
		return pos1.x() > pos2.x();
	}
	private boolean isPos1West() {
		return pos1.x() < pos2.x();
	}
	private boolean isPos1South() {
		return pos1.z() > pos2.z();
	}
	private boolean isPos1North() {
		return pos1.z() < pos2.z();
	}
	//</editor-fold>

	@Override
	public String toString() {
		return "from (%.1f, %.1f, %.1f) to (%.1f, %1f, %.1f)".formatted(
			pos1.x(), pos1.y(), pos1.z(),
			pos2.x(), pos2.y(), pos2.z()
		);
	}
}
