package com.aucguy.lateore.dev.command;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import com.aucguy.lateore.dev.LateOreDevMod;

/**
 * sets all the stone in a current chunk to TransparentStone or vice versia
 * 
 * @author aucguy
 */
public class CommandTransparent extends CommandBase {
	@Override
	public String getCommandName() {
		return "transstone";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/transstone <true|false>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length != 1) {
			throw new CommandException("incorrect number of arguments");
		}

		boolean way;
		if(args[0].startsWith("t")) {
			way = true;
		} else if(args[0].startsWith("f")) {
			way = false;
		} else {
			throw new CommandException("arg is not a boolean");
		}

		Block blockTo = way ? LateOreDevMod.instance.transStoneBlock : Blocks.stone;
		Block blockFrom = way ? Blocks.stone : LateOreDevMod.instance.transStoneBlock;
		replaceBlocks(sender, blockTo, blockFrom);
	}
	
	/**
	 * replaces block x with block y in the given chunk
	 */
	public static void replaceBlocks(ICommandSender sender, Block blockTo, Block blockFrom) {
		ChunkCoordinates pos = sender.getPlayerCoordinates();
		World world = sender.getEntityWorld();
		int startX = (pos.posX / 16) * 16 - (pos.posX < 0 ? 16 : 0);
		int startZ = (pos.posZ / 16) * 16 - (pos.posZ < 0 ? 16 : 0);
		int endX = startX + 16;
		int endZ = startZ + 16;
		
		for(int x = startX; x < endX; x++) {
			for(int y = 0; y < 255; y++) {
				for(int z = startZ; z < endZ; z++) {
					if(world.getBlock(x, y, z) == blockFrom) {
						world.setBlock(x, y, z, blockTo);
					}
				}
			}
		}
	}
}
