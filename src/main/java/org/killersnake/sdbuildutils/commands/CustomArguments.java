package org.killersnake.sdbuildutils.commands;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.killersnake.sdbuildutils.utils.Utils;

import java.util.Arrays;

public class CustomArguments {
	public static Argument<BlockData> InventoryBlocksArgument(String nodeName) {
		return new CustomArgument<BlockData, ItemStack>(new ItemStackArgument(nodeName), info -> {

			if (info.currentInput().getType().isSolid()) {
				return info.currentInput().getType().createBlockData();
			} else {
				throw CustomArgumentException.fromMessageBuilder(new MessageBuilder("Not a block: ").appendArgInput());
			}
		})
				.replaceSuggestions(ArgumentSuggestions.strings(info -> Arrays
						.stream(
								((Player) info.sender())
										.getInventory()
										.getContents()
						)
						.filter(item -> item != null && item.getType().isSolid() && !Utils.isBlacklisted(item.getType()))
						.map(item -> item.getType().toString().toLowerCase())
						.toArray(String[]::new)
				));
	}
}
