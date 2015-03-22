package com.aucguy.lateore.dev.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import com.aucguy.lateore.LateOreMod;
import com.aucguy.lateore.generate.ChunkHook;
import com.aucguy.lateore.util.ChunkData;

/**
 * sets all the stone in a current chunk to TransparentStone or vice versa
 * 
 * @author aucguy
 */
public class CommandLoadedChunks extends CommandBase {

	@Override
	public String getCommandName() {
		return "loadedchunks";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/loadedchunks";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		for(ChunkData chunkdata : LateOreMod.instance.chunkHook.chunksJustLoaded) {
			System.out.println(ChunkHook.getCoordsForChunk(chunkdata.getChunk()));
		}
	}
}
