package com.aucguy.lateore.dev.command;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

/**
 * sets all the stone in a current chunk to TransparentStone or vice versia
 * 
 * @author aucguy
 */
public class CommandCountBlock extends CommandBase {

	@Override
	public String getCommandName() {
		return "count";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/count <block>";
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
		
		ChunkCoordinates pos = sender.getPlayerCoordinates();
		World world = sender.getEntityWorld();
		int startX = (pos.posX / 16) * 16 - (pos.posX < 0 ? 16 : 0);
		int startZ = (pos.posZ / 16) * 16 - (pos.posZ < 0 ? 16 : 0);
		int endX = startX + 16;
		int endZ = startZ + 16;
		int count = 0;
		
		for(int x = startX; x < endX; x++) {
			for(int y = 0; y < 255; y++) {
				for(int z = startZ; z < endZ; z++) {
					if(world.getBlock(x, y, z) == block) {
						count++;
					}
				}
			}
		}
		
		sender.addChatMessage(new ChatComponentText("found "+count+" "+args[0]+"(s)"));
	}
}
