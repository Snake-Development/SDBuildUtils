package org.killersnake.sdbuildutils;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BlockStateArgument;
import dev.jorel.commandapi.arguments.LongArgument;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.killersnake.sdbuildutils.listeners.PlayerSelectionInputListener;
import org.killersnake.sdbuildutils.listeners.PlayerSelectionListener;
import org.killersnake.sdbuildutils.utils.Selection;
import org.killersnake.sdbuildutils.utils.SelectionManager;
import org.killersnake.sdbuildutils.utils.Utils;

public final class SDBuildUtils extends JavaPlugin {
	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();

		SelectionManager selectionManager = new SelectionManager(this);

		pm.registerEvents(new PlayerSelectionInputListener(selectionManager), this);
		pm.registerEvents(new PlayerSelectionListener(), this);

		//!TODO: https://jd.papermc.io/paper/1.21.11/org/bukkit/event/block/BlockPlaceEvent.html#canBuild()
		//! Implement Cancellable

		new CommandAPICommand("selection")
				.withSubcommands(
						new CommandAPICommand("build")
								.withArguments(
										//TODO: change to a custom argument, which only allows blocks from player's inventory (and their states)
										new BlockStateArgument("block"),
										new LongArgument("delay_per_block")
								)
								.executes((sender, args) -> {
									BlockState blockState = (BlockState) args.get("block");
									Player player = (Player) sender;
									Selection selection = selectionManager.getSelection(player);
									if (selection == null) {
										Utils.messagePlayer(
												sender.getName(),
												"No selection found!"
										);
									} else {
										if (selection.getPos2() == null) {
											Utils.messagePlayer(
													sender.getName(),
													"Selection is incomplete"
											);
										} else {
											Utils.messagePlayer(
													sender.getName(),
													"Filling %s [%s]".formatted(
															blockState
																	.getBlockData()
																	.getMaterial()
																	.toString(),
															selection.toString()
													));
											selectionManager.getSelection(player).fillSelection(player, blockState, (long) args.get("delay_per_block"));
											selectionManager.reset(player);
										}
									}
								}),
						new CommandAPICommand("cancel")
								.executes((sender, args) -> {
									Utils.messagePlayer(
											sender.getName(),
											"Cancelled the selection"
									);
									selectionManager.reset((Player) sender);
								}),
						new CommandAPICommand("resize")
								.withArguments(
										new MultiLiteralArgument("face", Arrays.stream(ResizeFace.values()).map(face -> face.toString().toLowerCase()).toArray(String[]::new)),
										new MultiLiteralArgument("operation", Arrays.stream(ResizeOperation.values()).map(op -> op.toString().toLowerCase()).toArray(String[]::new)),
										new IntegerArgument("amount", 1, 8)
								)
								.executes((sender, args) -> {
									ResizeFace face = ResizeFace.valueOf((String) args.get("face"));
									ResizeOperation operation = ResizeOperation.valueOf((String) args.get("operation"));
									int amount = (int) args.get("amount");

									selectionManager.resize((Player) sender, face, operation, amount);

									Utils.messagePlayer(
											sender.getName(),
											"Resized selection at face %s by %d blocks".formatted(
													face.toString(),
													(operation == ResizeOperation.DECREASE) ? -amount : amount
											)
									);
								})
				)
				.register(this);
	}
	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
