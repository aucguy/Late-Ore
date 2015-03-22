package com.aucguy.lateore.dev.command;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;

/**
 * sets all the stone in a current chunk to TransparentStone or vice versia
 * 
 * @author aucguy
 */
public class CommandRemoveBlock extends CommandBase {

	@Override
	public String getCommandName() {
		return "remove";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/remove <block>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length != 1) {
			throw new CommandException("incorrect number of arguments");
		}
		
		Block block = Block.getBlockFromName(args[0]);
		if(block == null) {
			throw new CommandException("invalid block");
		}

		CommandTransparent.replaceBlocks(sender, Blocks.air, block);
	}
}
